package az.xalqbank.mscustomers.controller;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerController {

    @GetMapping
    ResponseEntity<List<CustomerDTO>> getAllCustomers();

    @GetMapping("/{id}")
    ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id);

//    @GetMapping("/email/{email}")
//    ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email);

    // POST isteğinde multipart/form-data ile name, email, phoneNumber ve file parametreleri gönderilir.
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<CustomerDTO> addCustomer(@RequestParam String name,
                                            @RequestParam String email,
                                            @RequestParam String phoneNumber,
                                            @RequestParam("file") MultipartFile file) throws IOException;

    @PutMapping("/{id}")
    ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id,
                                               @RequestParam String name,
                                               @RequestParam String email,
                                               @RequestParam String phoneNumber,
                                               @RequestParam(value = "file", required = false) MultipartFile file) throws IOException;

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCustomer(@PathVariable Long id);

}
