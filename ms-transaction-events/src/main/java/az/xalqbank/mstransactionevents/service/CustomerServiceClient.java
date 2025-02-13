package az.xalqbank.mstransactionevents.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service client for interacting with the Customer service.
 */
@Service
public class CustomerServiceClient {

    private final RestTemplate restTemplate;

    public CustomerServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Checks if a customer exists by calling the Customer service.
     *
     * @param customerId the ID of the customer.
     * @return true if the customer exists, false otherwise.
     */
    public boolean isCustomerExists(Long customerId) {
        String url = "http://ms-customers:8085/api/v1/customers/" + customerId;
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
