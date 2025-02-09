package az.xalqbank.mscustomers.mapper;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Converts a Customer entity to a CustomerDTO.
     *
     * @param customer the customer entity
     * @return the corresponding CustomerDTO
     */
    CustomerDTO toDto(Customer customer);
}
