package az.xalqbank.mscustomers.mapper;

import az.xalqbank.mscustomers.dto.CustomerDTO;
import az.xalqbank.mscustomers.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    // Customer Entity'den CustomerDTO'ya dönüşüm
    CustomerDTO toDto(Customer customer);

    // CustomerDTO'dan Customer Entity'ye dönüşüm
    Customer toEntity(CustomerDTO customerDTO);
}
