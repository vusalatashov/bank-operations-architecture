package az.xalqbank.mscustomers.mapper;


import az.xalqbank.mscustomers.dto.request.CustomerRequest;
import az.xalqbank.mscustomers.dto.response.CustomerResponse;
import az.xalqbank.mscustomers.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Converts a Customer entity to a CustomerResponse.
     *
     * @param customer the customer entity
     * @return the corresponding CustomerResponse
     */
    CustomerResponse toResponse(Customer customer);

    /**
     * Converts a CustomerRequest to a Customer entity.
     *
     * @param customerRequest the customer request DTO
     * @return the corresponding Customer entity
     */
    Customer toEntity(CustomerRequest customerRequest);
}
