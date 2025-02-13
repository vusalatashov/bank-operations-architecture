package az.xalqbank.mstransactionevents.mapper;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.model.TransactionEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public class TransactionEventMapper {

    public TransactionEvent toEntity(TransactionEventDto dto){
        return TransactionEvent.builder()
                .customerId(dto.getCustomerId())
                .transactionType(dto.getTransactionType())
                .amount(dto.getAmount())
                .transactionDate(dto.getTransactionDate() != null ? dto.getTransactionDate() : LocalDateTime.now()) // Default to current time if null
                .status(dto.getStatus() != null ? dto.getStatus() : "PENDING")  // Ensure status is not null
                .build();
    }

    public TransactionEventDto toDto(TransactionEvent transactionEvent){
        return TransactionEventDto.builder()
                .customerId(transactionEvent.getCustomerId())
                .transactionType(transactionEvent.getTransactionType())
                .amount(transactionEvent.getAmount())
                .transactionDate(transactionEvent.getTransactionDate())
                .status(transactionEvent.getStatus())
                .build();
    }
}
