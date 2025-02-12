package az.xalqbank.msfileupload.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Client service for communicating with the Customer microservice.
 */
@Service
public class CustomerServiceClient {

    private final RestTemplate restTemplate;

    public CustomerServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Checks if a customer exists.
     *
     * @param customerId the customer ID.
     * @return true if the customer exists, false otherwise.
     */
    public boolean isCustomerExists(Long customerId) {
        String url = "http://localhost:8085/api/v1/customers/" + customerId;
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
