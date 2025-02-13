package az.xalqbank.mstransactionevents.service;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import java.util.List;

/**
 * Interface defining the contract for transaction event services.
 */
public interface ITransactionEventService {

    /**
     * Creates a new transaction event.
     *
     * @param transactionEventDto the transaction event data.
     * @return the created TransactionEventDto.
     */
    TransactionEventDto createTransactionEvent(TransactionEventDto transactionEventDto);

    /**
     * Retrieves all transaction events.
     *
     * @return a list of TransactionEventDto.
     */
    List<TransactionEventDto> getAllTransactionEvents();

    /**
     * Retrieves a transaction event by its ID.
     *
     * @param id the ID of the transaction event.
     * @return the corresponding TransactionEventDto.
     */

    TransactionEventDto getTransactionById(Long id);

}