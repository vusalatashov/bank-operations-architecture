package az.xalqbank.mstransactionevents.controller;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.service.ITransactionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionEventsController {

    private final ITransactionEventService transactionEventService;

    @PostMapping
    public ResponseEntity<TransactionEventDto> createTransaction(
            @RequestParam Long customerId,
            @RequestParam Double amount,
            @RequestParam(required = false) String transactionType,  // `transactionType` ekleyin
            @RequestParam(required = false) String status) {  // `status` ekleyin
        TransactionEventDto transactionEventDto = TransactionEventDto.builder()
                .customerId(customerId)
                .amount(amount)
                .transactionType(transactionType)
                .status(status)
                .build();

        TransactionEventDto createdTransaction = transactionEventService.createTransactionEvent(transactionEventDto);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }


    // Bütün transaction-ları gətir
    @GetMapping
    public ResponseEntity<List<TransactionEventDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionEventService.getAllTransactionEvents());
    }

    // ID-yə görə transaction-ları gətir
    @GetMapping("/{id}")
    public ResponseEntity<TransactionEventDto> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionEventService.getTransactionById(id));
    }
}
