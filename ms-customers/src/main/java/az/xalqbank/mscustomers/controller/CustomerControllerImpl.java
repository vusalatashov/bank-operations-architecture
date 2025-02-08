package az.xalqbank.mscustomers.controller;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;

    @Override
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
        return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        Optional<CustomerDTO> customerDTO = customerService.getCustomerById(id);
        return customerDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        Optional<CustomerDTO> customerDTO = customerService.getCustomerByEmail(email);
        return customerDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@RequestParam String name,
                                                   @RequestParam String email,
                                                   @RequestParam String phoneNumber) {
        CustomerDTO createdCustomer = customerService.addCustomer(name, email, phoneNumber);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id,
                                                      @RequestParam String name,
                                                      @RequestParam String email,
                                                      @RequestParam String phoneNumber) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, name, email, phoneNumber);
        if (updatedCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedCustomer);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
