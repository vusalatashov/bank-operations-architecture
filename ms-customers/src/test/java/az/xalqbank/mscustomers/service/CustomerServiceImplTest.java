package az.xalqbank.mscustomers.service;


import az.xalqbank.mscustomers.dto.request.CustomerRequest;
import az.xalqbank.mscustomers.dto.response.CustomerResponse;
import az.xalqbank.mscustomers.mapper.CustomerMapper;
import az.xalqbank.mscustomers.model.Customer;
import az.xalqbank.mscustomers.repository.CustomerRepository;
import az.xalqbank.mscustomers.publisher.PhotoUploadEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;
    private CustomerRepository customerRepository;
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private CustomerMapper customerMapper;
    private PhotoUploadEventPublisher photoUploadEventPublisher;

    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        redisTemplate = Mockito.mock(RedisTemplate.class);
        valueOperations = Mockito.mock(ValueOperations.class);  // Mock ValueOperations
        customerMapper = Mockito.mock(CustomerMapper.class);
        photoUploadEventPublisher = Mockito.mock(PhotoUploadEventPublisher.class);  // Mock PhotoUploadEventPublisher

        // Mock redisTemplate.opsForValue() to return valueOperations
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Create CustomerServiceImpl with the correct number of arguments
        customerService = new CustomerServiceImpl(customerRepository, redisTemplate, customerMapper, photoUploadEventPublisher);
    }



    @Test
    void testGetCustomerById_NotFound() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerResponse> result = customerService.getCustomerById(customerId);

        assertFalse(result.isPresent());
    }

    @Test
    void testAddCustomer() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe");
        customerRequest.setEmail("john@example.com");
        customerRequest.setPhoneNumber("1234567890");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhoneNumber("1234567890");

        CustomerResponse customerResponse = new CustomerResponse();
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponse(customer)).thenReturn(customerResponse);

        CustomerResponse result = customerService.addCustomer(customerRequest);

        assertNotNull(result);
        assertEquals(customerResponse, result);
    }

    @Test
    void testDeleteCustomer_Success() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        boolean result = customerService.deleteCustomer(customerId);

        assertTrue(result);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        boolean result = customerService.deleteCustomer(customerId);

        assertFalse(result);
    }
}
