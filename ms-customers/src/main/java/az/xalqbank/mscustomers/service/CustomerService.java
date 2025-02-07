package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    Optional<CustomerDTO> getCustomerById(Long id);
    Optional<CustomerDTO> getCustomerByEmail(String email);
    CustomerDTO addCustomer(String name, String email, String phoneNumber, MultipartFile file) throws IOException;
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO, MultipartFile file) throws IOException;
    boolean deleteCustomer(Long id);
    CustomerDTO uploadProfilePhoto(Long customerId, MultipartFile photoFile) throws IOException;
}
