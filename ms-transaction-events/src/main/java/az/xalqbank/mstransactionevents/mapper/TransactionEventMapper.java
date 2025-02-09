package az.xalqbank.mstransactionevents.mapper;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.model.TransactionEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionEventMapper {
    @Mapping(target = "transactionDate", expression = "java(dto.getTransactionDate() != null ? dto.getTransactionDate() : java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? dto.getStatus() : \"PENDING\")")
    @Mapping(target = "transactionType", expression = "java(dto.getTransactionType() != null ? dto.getTransactionType() : \"UNKNOWN\")")
    @Mapping(target = "customerId", source = "customerId")  // Explicit mapping for customerId
    TransactionEvent toEntity(TransactionEventDto dto);

    TransactionEventDto toDto(TransactionEvent transactionEvent);  // Eğer alan adları aynıysa bu yeterli olur
}
