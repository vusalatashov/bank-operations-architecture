package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.dto.ProfilePhotoDTO;
import az.xalqbank.mscustomers.model.Customer;
import az.xalqbank.mscustomers.model.ProfilePhoto;
import az.xalqbank.mscustomers.repository.CustomerRepository;
import az.xalqbank.mscustomers.repository.ProfilePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        String cacheKey = "customers:all";
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        // Redis'te önbelleğe alınmış müşteri listesi var mı?
        List<CustomerDTO> customers = (List<CustomerDTO>) valueOperations.get(cacheKey);
        if (customers != null) {
            return customers;
        }

        // Eğer Redis'te yoksa, veritabanından çek ve cache'e ekle
        customers = customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        valueOperations.set(cacheKey, customers, Duration.ofMinutes(10)); // 10 dakika cache'te tut
        return customers;
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long id) {
        String cacheKey = "customer:" + id;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        // Redis'te önbelleğe alınmış müşteri var mı?
        CustomerDTO cachedCustomer = (CustomerDTO) valueOperations.get(cacheKey);
        if (cachedCustomer != null) {
            return Optional.of(cachedCustomer);
        }

        // Eğer Redis'te yoksa, veritabanından çek ve cache'e ekle
        Optional<CustomerDTO> customerDTO = customerRepository.findById(id).map(this::convertToDTO);
        customerDTO.ifPresent(dto -> valueOperations.set(cacheKey, dto, Duration.ofMinutes(10)));

        return customerDTO;
    }

    @Override
    public Optional<CustomerDTO> getCustomerByEmail(String email) {
        String cacheKey = "customer:email:" + email;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        CustomerDTO cachedCustomer = (CustomerDTO) valueOperations.get(cacheKey);
        if (cachedCustomer != null) {
            return Optional.of(cachedCustomer);
        }

        Optional<CustomerDTO> customerDTO = customerRepository.findByEmail(email).map(this::convertToDTO);
        customerDTO.ifPresent(dto -> valueOperations.set(cacheKey, dto, Duration.ofMinutes(10)));

        return customerDTO;
    }

    @Override
    public CustomerDTO addCustomer(String name, String email, String phoneNumber, MultipartFile file) throws IOException {
        ProfilePhoto profilePhoto = processFileUpload(file);

        // Yeni müşteri oluşturuluyor
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setProfilePhoto(profilePhoto);

        // Müşteri veritabanına kaydediliyor
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDTO savedCustomerDTO = convertToDTO(savedCustomer);

        // Redis'e yeni müşteri ekleniyor
        String cacheKey = "customer:" + savedCustomer.getId();
        redisTemplate.opsForValue().set(cacheKey, savedCustomerDTO, Duration.ofMinutes(10));

        // Müşteri listesi cache'ini temizle
        redisTemplate.delete("customers:all");

        return savedCustomerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO, MultipartFile file) throws IOException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        // Mevcut müşteri bilgileri alınıyor
        Customer customer = optionalCustomer.get();
        updateCustomerDetails(customer, customerDTO);

        // Profil fotoğrafı güncelleniyor
        if (file != null && !file.isEmpty()) {
            updateProfilePhoto(customer, file);
        }

        // Güncellenmiş müşteri kaydediliyor
        Customer updatedCustomer = customerRepository.save(customer);
        CustomerDTO updatedCustomerDTO = convertToDTO(updatedCustomer);

        // Redis'teki cache'i güncelle
        String cacheKey = "customer:" + id;
        redisTemplate.opsForValue().set(cacheKey, updatedCustomerDTO, Duration.ofMinutes(10));

        // Müşteri listesi cache'ini temizle
        redisTemplate.delete("customers:all");

        return updatedCustomerDTO;
    }

    @Override
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);

            // Redis cache temizleme işlemi
            redisTemplate.delete("customer:" + id);
            redisTemplate.delete("customers:all");

            return true;
        }
        return false;
    }

    @Override
    public CustomerDTO uploadProfilePhoto(Long customerId, MultipartFile photoFile) throws IOException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer customer = optionalCustomer.get();
        ProfilePhoto profilePhoto = processFileUpload(photoFile);

        // Profil fotoğrafı ilişkilendiriliyor
        customer.setProfilePhoto(profilePhoto);

        // Güncellenmiş müşteri kaydediliyor
        Customer updatedCustomer = customerRepository.save(customer);
        CustomerDTO updatedCustomerDTO = convertToDTO(updatedCustomer);

        // Redis'e güncellenmiş müşteri bilgileri ekleniyor
        String cacheKey = "customer:" + customerId;
        redisTemplate.opsForValue().set(cacheKey, updatedCustomerDTO, Duration.ofMinutes(10));

        return updatedCustomerDTO;
    }

    private void updateCustomerDetails(Customer customer, CustomerDTO customerDTO) {
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
    }

    private void updateProfilePhoto(Customer customer, MultipartFile file) throws IOException {
        ProfilePhoto profilePhoto = customer.getProfilePhoto();

        if (profilePhoto != null) {
            profilePhoto.setFileName(file.getOriginalFilename());
            profilePhoto.setFileUrl("http://localhost:8080/files/" + file.getOriginalFilename());
            profilePhoto.setSize(file.getSize());
            profilePhoto.setFormat(getFileExtension(file.getOriginalFilename()));
        } else {
            profilePhoto = processFileUpload(file);
        }

        profilePhotoRepository.save(profilePhoto);
        customer.setProfilePhoto(profilePhoto);
        customerRepository.save(customer);
    }

    private ProfilePhoto processFileUpload(MultipartFile file) throws IOException {
        String uploadDir = "/Users/macbook/Desktop/xalq-bank-tasks/ms-customers/src/main/resources/uploads/";

        // Dosya yükleme klasörünü oluşturuyoruz
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        File destinationFile = new File(uploadDir + originalFileName);
        file.transferTo(destinationFile);

        // Profil fotoğrafı nesnesi oluşturuluyor
        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setFileName(originalFileName);
        profilePhoto.setSize(file.getSize());
        profilePhoto.setFormat(getFileExtension(originalFileName));
        profilePhoto.setFileUrl("http://localhost:8080/files/" + originalFileName);
        return profilePhoto;
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

    private CustomerDTO convertToDTO(Customer customer) {
        ProfilePhotoDTO profilePhotoDTO = null;
        if (customer.getProfilePhoto() != null) {
            ProfilePhoto profilePhoto = customer.getProfilePhoto();  // Single photo as it's One-to-One
            profilePhotoDTO = new ProfilePhotoDTO(
                    profilePhoto.getId(),
                    profilePhoto.getFileName(),
                    profilePhoto.getFileUrl(),
                    profilePhoto.getSize(),
                    profilePhoto.getFormat()
            );
        }

        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                profilePhotoDTO  // Single profile photo DTO
        );
    }
}
