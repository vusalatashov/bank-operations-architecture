package az.xalqbank.mscustomers.service;

import az.xalqbank.mscustomers.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(Long id);

    CustomerDTO addCustomer(String name, String email, String phoneNumber);

    boolean deleteCustomer(Long id);

    void updateCustomerProfilePhoto(Long customerId, String fileUrl);
}