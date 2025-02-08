package az.xalqbank.mstransactionevents.service.impl;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.integration.RabbitMQPublisher;
import az.xalqbank.mstransactionevents.model.TransactionEvent;
import az.xalqbank.mstransactionevents.repository.TransactionEventRepository;
import az.xalqbank.mstransactionevents.service.ITransactionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionEventService implements ITransactionEventService {

    private final TransactionEventRepository transactionEventRepository;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Override
    public TransactionEventDto createTransactionEvent(TransactionEventDto transactionEventDto) {
        TransactionEvent transactionEvent = TransactionEvent.builder()
                .customerId(transactionEventDto.getCustomerId())
                .transactionType(transactionEventDto.getTransactionType())
                .amount(transactionEventDto.getAmount())
                .transactionDate(transactionEventDto.getTransactionDate())
                .status(transactionEventDto.getStatus())
                .build();

        TransactionEvent savedEvent = transactionEventRepository.save(transactionEvent);
        TransactionEventDto savedDto = mapToDto(savedEvent);

        // RabbitMQ-ya mesaj göndər
        rabbitMQPublisher.publishTransactionEvent(savedDto);

        return savedDto;
    }

    @Override
    public List<TransactionEventDto> getAllTransactionEvents() {
        List<TransactionEvent> events = transactionEventRepository.findAll();
        return events.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TransactionEventDto getTransactionById(Long id) {
        TransactionEvent event = transactionEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
        return mapToDto(event);
    }

    private TransactionEventDto mapToDto(TransactionEvent transactionEvent) {
        return TransactionEventDto.builder()
                .customerId(transactionEvent.getCustomerId())
                .transactionType(transactionEvent.getTransactionType())
                .amount(transactionEvent.getAmount())
                .transactionDate(transactionEvent.getTransactionDate())
                .status(transactionEvent.getStatus())
                .build();
    }
}
