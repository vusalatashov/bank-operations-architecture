package az.xalqbank.mscustomers.controller;


import az.xalqbank.mscustomers.dto.request.CustomerRequest;
import az.xalqbank.mscustomers.dto.response.CustomerResponse;
import az.xalqbank.mscustomers.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerController customerController;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = Mockito.mock(CustomerService.class);
        customerController = new CustomerController(customerService);
    }

    @Test
    void testGetAllCustomers() {
        // Mock the service call
        CustomerResponse customerResponse = new CustomerResponse();
        when(customerService.getAllCustomers()).thenReturn(Collections.singletonList(customerResponse));

        // Call the method
        ResponseEntity<List<CustomerResponse>> response = customerController.getAllCustomers();

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetCustomerById_Found() {
        Long customerId = 1L;
        CustomerResponse customerResponse = new CustomerResponse();
        when(customerService.getCustomerById(customerId)).thenReturn(java.util.Optional.of(customerResponse));

        ResponseEntity<CustomerResponse> response = customerController.getCustomerById(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCustomerById_NotFound() {
        Long customerId = 1L;
        when(customerService.getCustomerById(customerId)).thenReturn(java.util.Optional.empty());

        ResponseEntity<CustomerResponse> response = customerController.getCustomerById(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddCustomer() {
        CustomerRequest customerRequest = new CustomerRequest();
        CustomerResponse customerResponse = new CustomerResponse();
        when(customerService.addCustomer(customerRequest)).thenReturn(customerResponse);

        ResponseEntity<CustomerResponse> response = customerController.addCustomer(customerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteCustomer_Success() {
        Long customerId = 1L;
        when(customerService.deleteCustomer(customerId)).thenReturn(true);

        ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteCustomer_NotFound() {
        Long customerId = 1L;
        when(customerService.deleteCustomer(customerId)).thenReturn(false);

        ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}
