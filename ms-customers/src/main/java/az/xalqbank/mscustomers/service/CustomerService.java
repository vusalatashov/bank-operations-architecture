package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(Long id);

    Optional<CustomerDTO> getCustomerByEmail(String email);

    CustomerDTO addCustomer(String name, String email, String phoneNumber);

    CustomerDTO updateCustomer(Long id, String name, String email, String phoneNumber);

    boolean deleteCustomer(Long id);

    CustomerDTO uploadProfilePhoto(Long customerId);

    CustomerDTO updateCustomerProfilePhoto(Long customerId, String fileUrl);
}