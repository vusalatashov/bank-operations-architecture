package az.xalqbank.mstransactionevents.service;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.exception.CustomerNotFoundException;
import az.xalqbank.mstransactionevents.exception.InvalidRequestException;
import az.xalqbank.mstransactionevents.exception.ResourceNotFoundException;
import az.xalqbank.mstransactionevents.exception.TransactionNotFoundException;
import az.xalqbank.mstransactionevents.integration.RabbitMQPublisher;
import az.xalqbank.mstransactionevents.mapper.TransactionEventMapper;
import az.xalqbank.mstransactionevents.model.TransactionEvent;
import az.xalqbank.mstransactionevents.repository.TransactionEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing transaction events.
 */
@Service
@RequiredArgsConstructor
public class TransactionEventService implements ITransactionEventService {

    private final TransactionEventRepository transactionEventRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private final CustomerServiceClient customerServiceClient;
    private final TransactionEventMapper transactionEventMapper;

    /**
     * Creates a new transaction event after validating the request.
     *
     * @param transactionEventDto the transaction event data.
     * @return the created TransactionEventDto.
     * @throws InvalidRequestException   if the amount is invalid.
     * @throws CustomerNotFoundException if the customer does not exist.
     */
    @Override
    public TransactionEventDto createTransactionEvent(TransactionEventDto transactionEventDto) {
        // Validate amount
        if (transactionEventDto.getAmount() == null || transactionEventDto.getAmount() <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero.");
        }

        // Validate customer existence
        if (!customerServiceClient.isCustomerExists(transactionEventDto.getCustomerId())) {
            throw new CustomerNotFoundException("Customer not found with ID: " + transactionEventDto.getCustomerId());
        }

        // Convert DTO to entity and save
        TransactionEvent transactionEvent = transactionEventMapper.toEntity(transactionEventDto);
        TransactionEvent savedEvent = transactionEventRepository.save(transactionEvent);
        TransactionEventDto savedDto = transactionEventMapper.toDto(savedEvent);

        // Publish event to RabbitMQ
        rabbitMQPublisher.publishTransactionEvent(savedDto);

        return savedDto;
    }

    /**
     * Retrieves all transaction events.
     *
     * @return a list of TransactionEventDto.
     */
    @Override
    public List<TransactionEventDto> getAllTransactionEvents() {
        return transactionEventRepository.findAll()
                .stream().map(transactionEventMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a transaction event by its ID.
     *
     * @param id the ID of the transaction event.
     * @return the corresponding TransactionEventDto.
     * @throws TransactionNotFoundException if the transaction is not found.
     */
    @Override
    public TransactionEventDto getTransactionById(Long id) {
        return transactionEventRepository.findById(id)
                .map(transactionEventMapper::toDto)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + id));
    }


}
