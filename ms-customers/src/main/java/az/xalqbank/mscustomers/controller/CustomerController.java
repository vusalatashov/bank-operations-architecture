package az.xalqbank.mscustomers.controller;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller implementation for managing customer-related operations.
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Retrieves all customers.
     *
     * @return ResponseEntity containing the list of customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
        return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
    }

    /**
     * Retrieves a customer by their unique ID.
     *
     * @param id The unique identifier of the customer
     * @return ResponseEntity containing the customer data if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        Optional<CustomerDTO> customerDTO = customerService.getCustomerById(id);
        return customerDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * Adds a new customer.
     *
     * @param customerDTO Customer data for the new customer
     * @return ResponseEntity containing the created customer
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.addCustomer(customerDTO.getName(),
                customerDTO.getEmail(),
                customerDTO.getPhoneNumber());
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }


    /**
     * Deletes a customer by their ID.
     *
     * @param id The unique identifier of the customer
     * @return ResponseEntity with status code based on success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
