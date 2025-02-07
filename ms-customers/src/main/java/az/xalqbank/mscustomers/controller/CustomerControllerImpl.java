package az.xalqbank.mscustomers.controller;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
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

//    @Override
//    @GetMapping("/email/{email}")
//    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
//        Optional<CustomerDTO> customerDTO = customerService.getCustomerByEmail(email);
//        return customerDTO.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }

    @Override
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CustomerDTO> addCustomer(@RequestParam String name,
                                                   @RequestParam String email,
                                                   @RequestParam String phoneNumber,
                                                   @RequestParam("file") MultipartFile file) throws IOException {

        CustomerDTO createdCustomer = customerService.addCustomer(name, email, phoneNumber, file);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id,
                                                      @RequestParam String name,
                                                      @RequestParam String email,
                                                      @RequestParam String phoneNumber,
                                                      @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(name);
        customerDTO.setEmail(email);
        customerDTO.setPhoneNumber(phoneNumber);

        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO, file);
        if (updatedCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedCustomer);
    }


    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        return deleted ? ResponseEntity.noContent().build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
