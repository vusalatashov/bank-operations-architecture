package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.mapper.CustomerMapper;
import az.xalqbank.mscustomers.model.Customer;
import az.xalqbank.mscustomers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        String cacheKey = "customers:all";
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        List<CustomerDTO> customers = (List<CustomerDTO>) valueOperations.get(cacheKey);
        if (customers != null) {
            return customers;
        }

        customers = customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());

        valueOperations.set(cacheKey, customers, Duration.ofMinutes(10));
        return customers;
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long id) {
        String cacheKey = "customer:" + id;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        CustomerDTO cachedCustomer = (CustomerDTO) valueOperations.get(cacheKey);
        if (cachedCustomer != null) {
            return Optional.of(cachedCustomer);
        }

        Optional<CustomerDTO> customerDTO = customerRepository.findById(id)
                .map(customerMapper::toDto);

        customerDTO.ifPresent(dto ->
                valueOperations.set(cacheKey, dto, Duration.ofMinutes(10))
        );
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

        Optional<CustomerDTO> customerDTO = customerRepository
                .findByEmail(email).map(customerMapper::toDto);

        customerDTO.ifPresent(dto ->
                valueOperations.set(cacheKey, dto, Duration.ofMinutes(10))
        );
        return customerDTO;
    }

    @Override
    public CustomerDTO addCustomer(String name, String email, String phoneNumber) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);

        Customer savedCustomer = customerRepository.save(customer);
        CustomerDTO savedCustomerDTO = customerMapper.toDto(savedCustomer);

        String cacheKey = "customer:" + savedCustomer.getId();
        redisTemplate.opsForValue().set(cacheKey, savedCustomerDTO, Duration.ofMinutes(10));

        redisTemplate.delete("customers:all");

        return savedCustomerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, String name, String email, String phoneNumber) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer customer = optionalCustomer.get();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);

        Customer updatedCustomer = customerRepository.save(customer);
        CustomerDTO updatedCustomerDTO = customerMapper.toDto(updatedCustomer);

        String cacheKey = "customer:" + id;
        redisTemplate.opsForValue().set(cacheKey, updatedCustomerDTO, Duration.ofMinutes(10));

        redisTemplate.delete("customers:all");
        return updatedCustomerDTO;
    }

    @Override
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);

            redisTemplate.delete("customer:" + id);
            redisTemplate.delete("customers:all");
            return true;
        }
        return false;
    }

    @Override
    public CustomerDTO uploadProfilePhoto(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer customer = optionalCustomer.get();

        Customer updatedCustomer = customerRepository.save(customer);
        CustomerDTO updatedCustomerDTO = customerMapper.toDto(updatedCustomer);

        String cacheKey = "customer:" + customerId;
        redisTemplate.opsForValue().set(cacheKey, updatedCustomerDTO, Duration.ofMinutes(10));
        return updatedCustomerDTO;
    }
    public CustomerDTO updateCustomerProfilePhoto(Long customerId, String fileUrl) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer customer = optionalCustomer.get();
        customer.setProfilePhotoUrl(fileUrl); // Profil fotosunu set edirik ✅

        Customer updatedCustomer = customerRepository.save(customer); // Yenilənmiş obyekti bazaya yazırıq ✅
        CustomerDTO updatedCustomerDTO = customerMapper.toDto(updatedCustomer);

        // Redis Cache-i yeniləyirik
        String cacheKey = "customer:" + customerId;
        redisTemplate.opsForValue().set(cacheKey, updatedCustomerDTO, Duration.ofMinutes(10));

        System.out.println("✅ [ms-customer] Profile photo updated in database for customer ID: " + customerId);

        return updatedCustomerDTO;
    }

}
