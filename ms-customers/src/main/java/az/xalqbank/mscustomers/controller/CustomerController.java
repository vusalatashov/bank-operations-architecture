package az.xalqbank.mscustomers.controller;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface CustomerController {

    ResponseEntity<List<CustomerDTO>> getAllCustomers();

    ResponseEntity<CustomerDTO> getCustomerById(Long id);

    ResponseEntity<CustomerDTO> getCustomerByEmail(String email);

    ResponseEntity<CustomerDTO> addCustomer(String name, String email, String phoneNumber) throws IOException;

    ResponseEntity<CustomerDTO> updateCustomer(Long id, String name, String email, String phoneNumber) throws IOException;

    ResponseEntity<Void> deleteCustomer(Long id);
}