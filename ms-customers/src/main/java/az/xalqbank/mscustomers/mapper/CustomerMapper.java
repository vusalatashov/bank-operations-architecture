package az.xalqbank.mscustomers.mapper;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO toDto(Customer customer);

    Customer toEntity(CustomerDTO customerDTO);
}