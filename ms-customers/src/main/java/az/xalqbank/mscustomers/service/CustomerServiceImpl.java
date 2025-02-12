package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.request.CustomerRequest;
import az.xalqbank.mscustomers.dto.response.CustomerResponse;
import az.xalqbank.mscustomers.mapper.CustomerMapper;
import az.xalqbank.mscustomers.model.Customer;
import az.xalqbank.mscustomers.publisher.PhotoUploadEventPublisher;
import az.xalqbank.mscustomers.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerService interface.
 * Provides functionality for managing customer data, including
 * CRUD operations, caching, and profile photo update handling.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomerMapper customerMapper;
    private final PhotoUploadEventPublisher photoUploadEventPublisher;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    /**
     * Constructor for CustomerServiceImpl. Initializes necessary components.
     *
     * @param customerRepository Repository for performing CRUD operations on customers.
     * @param redisTemplate Redis template for caching customer data.
     * @param customerMapper Mapper for converting between Customer and CustomerResponse objects.
     * @param photoUploadEventPublisher Publisher for handling photo upload events.
     */
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               RedisTemplate<String, String> redisTemplate,
                               CustomerMapper customerMapper,
                               PhotoUploadEventPublisher photoUploadEventPublisher) {
        this.customerRepository = customerRepository;
        this.redisTemplate = redisTemplate;
        this.customerMapper = customerMapper;
        this.photoUploadEventPublisher = photoUploadEventPublisher;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves a list of all customers.
     * Tries to fetch the customer list from the Redis cache first.
     * If not found, it queries the database and caches the result.
     *
     * @return List of CustomerResponse objects.
     */
    @Override
    public List<CustomerResponse> getAllCustomers() {
        String cacheKey = "customers:all";
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // Attempt to fetch customers from cache
        String cachedCustomersJson = valueOperations.get(cacheKey);
        if (cachedCustomersJson != null) {
            try {
                List<CustomerResponse> cachedCustomers = objectMapper.readValue(
                        cachedCustomersJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, CustomerResponse.class)
                );
                logger.info("Returning cached customers.");
                return cachedCustomers;
            } catch (Exception e) {
                logger.error("Error deserializing cached customers", e);
            }
        }

        // Fetch from database and cache the result
        List<CustomerResponse> customers = customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());

        try {
            String customersJson = objectMapper.writeValueAsString(customers);
            valueOperations.set(cacheKey, customersJson, Duration.ofMinutes(10));
            logger.info("Fetched all customers from database and cached them.");
        } catch (Exception e) {
            logger.error("Error serializing customers to cache", e);
        }

        return customers;
    }

    /**
     * Retrieves a customer by their ID.
     * Checks the cache first, if not found, queries the database and caches the result.
     *
     * @param id The ID of the customer to retrieve.
     * @return CustomerResponse wrapped in Optional.
     */
    @Override
    public Optional<CustomerResponse> getCustomerById(Long id) {
        String cacheKey = "customer:" + id;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // Try to fetch the customer from the cache
        String cachedCustomerJson = valueOperations.get(cacheKey);
        if (cachedCustomerJson != null) {
            try {
                CustomerResponse cachedCustomer = objectMapper.readValue(cachedCustomerJson, CustomerResponse.class);
                logger.info("Returning cached customer with ID: {}", id);
                return Optional.of(cachedCustomer);
            } catch (Exception e) {
                logger.error("Error deserializing cached customer with ID: {}", id, e);
            }
        }

        // If not in cache, fetch from database
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            CustomerResponse customerResponse = customerMapper.toResponse(customer);

            try {
                String customerJson = objectMapper.writeValueAsString(customerResponse);
                valueOperations.set(cacheKey, customerJson, Duration.ofMinutes(10));
                logger.info("Fetched customer with ID: {} from database and cached it.", id);
            } catch (Exception e) {
                logger.error("Error serializing customer to cache with ID: {}", id, e);
            }

            return Optional.of(customerResponse);
        }

        logger.warn("Customer with ID: {} not found", id);
        return Optional.empty();
    }

    /**
     * Adds a new customer to the system.
     * Saves the customer to the database and caches the customer data.
     * Invalidates the cache for all customers after adding the new one.
     *
     * @param customerRequest The customer data to add.
     * @return The created CustomerResponse.
     */
    @Override
    public CustomerResponse addCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());

        // Save customer to the database
        Customer savedCustomer = customerRepository.save(customer);
        CustomerResponse savedCustomerResponse = customerMapper.toResponse(savedCustomer);

        String cacheKey = "customer:" + savedCustomer.getId();
        try {
            String customerJson = objectMapper.writeValueAsString(savedCustomerResponse);
            redisTemplate.opsForValue().set(cacheKey, customerJson, Duration.ofMinutes(10));
        } catch (Exception e) {
            logger.error("Error serializing customer to cache with ID: {}", savedCustomer.getId(), e);
        }

        // Invalidate the cache for all customers
        redisTemplate.delete("customers:all");

        logger.info("Added new customer with ID: {}", savedCustomer.getId());
        return savedCustomerResponse;
    }

    /**
     * Deletes a customer by their ID.
     * If the customer exists, it deletes the customer from the database and invalidates their cache.
     * It also publishes an event to delete the associated profile photo.
     *
     * @param id The ID of the customer to delete.
     * @return True if the customer was successfully deleted, otherwise False.
     */
    @Override
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            // Publish event to delete photo
            photoUploadEventPublisher.publishDeletePhotoEvent(id);
            // Delete customer from the database
            customerRepository.deleteById(id);

            // Invalidate cache for the customer and all customers
            redisTemplate.delete("customer:" + id);
            redisTemplate.delete("customers:all");

            logger.info("Deleted customer with ID: {}", id);
            return true;
        }

        logger.warn("Attempt to delete non-existing customer with ID: {}", id);
        return false;
    }

    /**
     * Updates the profile photo of a customer.
     * Finds the customer by ID, updates the photo URL, and saves the updated customer.
     * The result is cached, and the profile photo is updated.
     *
     * @param customerId The ID of the customer.
     * @param fileUrl The URL of the new profile photo.
     */
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
        CustomerResponse updatedCustomerResponse = customerMapper.toResponse(updatedCustomer);

        String cacheKey = "customer:" + customerId;
        try {
            String customerJson = objectMapper.writeValueAsString(updatedCustomerResponse);
            redisTemplate.opsForValue().set(cacheKey, customerJson, Duration.ofMinutes(10));
        } catch (Exception e) {
            logger.error("Error serializing customer to cache with ID: {}", customerId, e);
        }

        logger.info("Profile photo updated for customer with ID: {}", customerId);
    }
}
