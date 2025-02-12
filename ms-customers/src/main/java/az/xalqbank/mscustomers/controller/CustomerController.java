package az.xalqbank.mscustomers.controller;


import az.xalqbank.mscustomers.dto.request.CustomerRequest;
import az.xalqbank.mscustomers.dto.response.CustomerResponse;
import az.xalqbank.mscustomers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing customer operations.
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Retrieves all customers.
     *
     * @return List of customers wrapped in ResponseEntity with HTTP status 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customerResponses = customerService.getAllCustomers();
        return new ResponseEntity<>(customerResponses, HttpStatus.OK);
    }

    /**
     * Retrieves a customer by ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return Customer details if found, otherwise HTTP 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        Optional<CustomerResponse> customerResponse = customerService.getCustomerById(id);
        return customerResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new customer.
     *
     * @param customerRequest The customer data for creation.
     * @return Created customer details wrapped in ResponseEntity with HTTP status 201 Created.
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse createdCustomer = customerService.addCustomer(customerRequest);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id The ID of the customer to delete.
     * @return HTTP 204 No Content if deletion was successful, otherwise HTTP 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
