package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.mapper.CustomerMapper;
import az.xalqbank.mscustomers.model.Customer;
import az.xalqbank.mscustomers.publisher.PhotoUploadEventPublisher;
import az.xalqbank.mscustomers.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RedisTemplate<String, CustomerDTO> redisTemplate;
    private final CustomerMapper customerMapper;
    private final PhotoUploadEventPublisher photoUploadEventPublisher;

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public CustomerServiceImpl(CustomerRepository customerRepository, RedisTemplate<String, CustomerDTO> redisTemplate, CustomerMapper customerMapper, PhotoUploadEventPublisher photoUploadEventPublisher) {
        this.customerRepository = customerRepository;
        this.redisTemplate = redisTemplate;
        this.customerMapper = customerMapper;
        this.photoUploadEventPublisher = photoUploadEventPublisher;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        String cacheKey = "customers:all";
        ValueOperations<String, CustomerDTO> valueOperations = redisTemplate.opsForValue();
        List<CustomerDTO> customers = (List<CustomerDTO>) valueOperations.get(cacheKey);

        if (customers != null) {
            logger.info("Returning cached customers.");
            return customers;
        }

        customers = customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());

        valueOperations.set(cacheKey, (CustomerDTO) customers, Duration.ofMinutes(10));
        logger.info("Fetched all customers from database and cached them.");
        return customers;
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long id) {
        String cacheKey = "customer:" + id;
        ValueOperations<String, CustomerDTO> valueOperations = redisTemplate.opsForValue();

        CustomerDTO cachedCustomer = (CustomerDTO) valueOperations.get(cacheKey);
        if (cachedCustomer != null) {
            logger.info("Returning cached customer with ID: {}", id);
            return Optional.of(cachedCustomer);
        }

        Optional<CustomerDTO> customerDTO = customerRepository.findById(id)
                .map(customerMapper::toDto);

        customerDTO.ifPresent(dto -> {
            valueOperations.set(cacheKey, dto, Duration.ofMinutes(10));
            logger.info("Fetched customer with ID: {} from database and cached it.", id);
        });
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

        logger.info("Added new customer with ID: {}", savedCustomer.getId());
        return savedCustomerDTO;
    }

    @Override
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            photoUploadEventPublisher.publishDeletePhotoEvent(id);
            customerRepository.deleteById(id);

            redisTemplate.delete("customer:" + id);
            redisTemplate.delete("customers:all");

            logger.info("Deleted customer with ID: {}", id);
            return true;
        }
        logger.warn("Attempt to delete non-existing customer with ID: {}", id);
        return false;
    }

    @Override
    public void updateCustomerProfilePhoto(Long customerId, String fileUrl) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            logger.error("Customer with ID: {} not found for profile photo update.", customerId);
            return;
        }

        Customer customer = optionalCustomer.get();
        customer.setProfilePhotoUrl(fileUrl);

        Customer updatedCustomer = customerRepository.save(customer);
        CustomerDTO updatedCustomerDTO = customerMapper.toDto(updatedCustomer);

        String cacheKey = "customer:" + customerId;
        redisTemplate.opsForValue().set(cacheKey, updatedCustomerDTO, Duration.ofMinutes(10));

        logger.info("Profile photo updated for customer with ID: {}", customerId);
    }
}
