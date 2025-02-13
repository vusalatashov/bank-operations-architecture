package az.xalqbank.mstransactionevents.controller;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.service.ITransactionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing transaction events.
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionEventsController {

    private final ITransactionEventService transactionEventService;

    /**
     * Creates a new transaction event.
     *
     * @param customerId      the ID of the customer.
     * @param amount          the transaction amount.
     * @param transactionType the type of the transaction (optional).
     * @param status          the status of the transaction (optional).
     * @return the created TransactionEventDto.
     */
    @PostMapping
    public ResponseEntity<TransactionEventDto> createTransaction(
            @RequestParam Long customerId,
            @RequestParam Double amount,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String status) {


        TransactionEventDto transactionEventDto = TransactionEventDto.builder()
                .customerId(customerId)
                .amount(amount)
                .transactionType(transactionType)
                .status(status)
                .build();

        TransactionEventDto createdTransaction = transactionEventService.createTransactionEvent(transactionEventDto);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    /**
     * Retrieves all transaction events.
     *
     * @return a list of TransactionEventDto.
     */
    @GetMapping
    public ResponseEntity<List<TransactionEventDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionEventService.getAllTransactionEvents());
    }

    /**
     * Retrieves a transaction event by its ID.
     *
     * @param id the ID of the transaction event.
     * @return the TransactionEventDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionEventDto> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionEventService.getTransactionById(id));
    }


}
