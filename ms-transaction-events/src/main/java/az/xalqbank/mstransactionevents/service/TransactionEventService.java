package az.xalqbank.mstransactionevents.service;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.integration.RabbitMQPublisher;
import az.xalqbank.mstransactionevents.mapper.TransactionEventMapper;
import az.xalqbank.mstransactionevents.model.TransactionEvent;
import az.xalqbank.mstransactionevents.repository.TransactionEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionEventService implements ITransactionEventService {

    private final TransactionEventRepository transactionEventRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private final CustomerServiceClient customerServiceClient;
    private final TransactionEventMapper transactionEventMapper;

    @Override
    public TransactionEventDto createTransactionEvent(TransactionEventDto transactionEventDto) {
        if (!customerServiceClient.isCustomerExists(transactionEventDto.getCustomerId())) {
            throw new RuntimeException("Customer not found with ID: " + transactionEventDto.getCustomerId());
        }

        TransactionEvent transactionEvent = transactionEventMapper.toEntity(transactionEventDto);
        TransactionEvent savedEvent = transactionEventRepository.save(transactionEvent);
        TransactionEventDto savedDto = transactionEventMapper.toDto(savedEvent);

        rabbitMQPublisher.publishTransactionEvent(savedDto);

        return savedDto;
    }

    @Override
    public List<TransactionEventDto> getAllTransactionEvents() {
        return transactionEventRepository.findAll()
                .stream().map(transactionEventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionEventDto getTransactionById(Long id) {
        return transactionEventRepository.findById(id)
                .map(transactionEventMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
    }
}
