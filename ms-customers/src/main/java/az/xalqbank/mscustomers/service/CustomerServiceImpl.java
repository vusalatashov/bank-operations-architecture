package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.dto.ProfilePhotoDTO;
import az.xalqbank.mscustomers.model.Customer;
import az.xalqbank.mscustomers.model.ProfilePhoto;
import az.xalqbank.mscustomers.repository.CustomerRepository;
import az.xalqbank.mscustomers.repository.ProfilePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ProfilePhotoRepository profilePhotoRepository;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<CustomerDTO> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).map(this::convertToDTO);
    }

    @Override
    public CustomerDTO addCustomer(String name, String email, String phoneNumber, MultipartFile file) throws IOException {
        // Profil fotoğrafı işleme
        ProfilePhoto profilePhoto = processFileUpload(file);

        // Yeni müşteri oluşturuluyor
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);

        // Profil fotoğrafı ilişkilendiriliyor
        customer.setProfilePhoto(profilePhoto);  // One-to-one ilişki kuruldu

        // Müşteri veritabanına kaydediliyor
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
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

        // Eğer yeni bir dosya yüklenmişse, profil fotoğrafını güncelle
        if (file != null && !file.isEmpty()) {
            updateProfilePhoto(customer, file);
        }

        // Güncellenmiş müşteri kaydediliyor
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    // Müşteri bilgilerini güncelleyen metod
    private void updateCustomerDetails(Customer customer, CustomerDTO customerDTO) {
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
    }
    private void updateProfilePhoto(Customer customer, MultipartFile file) throws IOException {
        // Eski profil fotoğrafını al
        ProfilePhoto profilePhoto = customer.getProfilePhoto();

        // Əgər müşteri artıq profil şəklinə sahibdirsə, mövcud profil şəklini yeniləyin
        if (profilePhoto != null) {
            profilePhoto.setFileName(file.getOriginalFilename());
            profilePhoto.setFileUrl("http://localhost:8080/files/" + file.getOriginalFilename());
            profilePhoto.setSize(file.getSize());
            profilePhoto.setFormat(getFileExtension(file.getOriginalFilename()));
        } else {
            // Əks halda yeni profil şəkli yaradın
            profilePhoto = processFileUpload(file);
        }

        // Yeni/mövcud profil şəkli saxlanır
        profilePhotoRepository.save(profilePhoto);

        // Müştəri obyektini yeniləyin
        customer.setProfilePhoto(profilePhoto);
        customerRepository.save(customer);
    }

    private void deleteOldProfilePhoto(ProfilePhoto oldProfilePhoto) {
        // Köhnə faylın yolunu təyin et
        String filePath = "/Users/macbook/Desktop/xalq-bank-tasks/ms-customers/src/main/resources/uploads/"
                + oldProfilePhoto.getFileName();
        File oldFile = new File(filePath);

        // Fayl varsa və silinə bilirsə, sil
        if (oldFile.exists() && oldFile.isFile()) {
            try {
                Files.delete(oldFile.toPath());
            } catch (IOException e) {
                System.err.println("Fayl silinərkən xəta: " + e.getMessage());
            }
        }
    }



    @Override
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
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
        customer.setProfilePhoto(profilePhoto);  // Yeni fotoğrafı ilişkilendiriyoruz

        // Güncellenmiş müşteri kaydediliyor
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
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

    // Dosya uzantısını alma işlemi
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
