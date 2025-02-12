package az.xalqbank.mscustomers.service;



import az.xalqbank.mscustomers.dto.request.CustomerRequest;
import az.xalqbank.mscustomers.dto.response.CustomerResponse;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerResponse> getAllCustomers();

    Optional<CustomerResponse> getCustomerById(Long id);

    CustomerResponse addCustomer(CustomerRequest customerRequest);

    boolean deleteCustomer(Long id);

    void updateCustomerProfilePhoto(Long customerId, String fileUrl);
}
