package az.xalqbank.mstransactionevents.service;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import java.util.List;

public interface ITransactionEventService {

    TransactionEventDto createTransactionEvent(TransactionEventDto transactionEventDto);

    List<TransactionEventDto> getAllTransactionEvents();

    TransactionEventDto getTransactionById(Long id);
}
