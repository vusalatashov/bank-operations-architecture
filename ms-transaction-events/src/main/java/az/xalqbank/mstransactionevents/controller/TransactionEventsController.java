package az.xalqbank.mstransactionevents.controller;

import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import az.xalqbank.mstransactionevents.service.ITransactionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionEventsController {

    private final ITransactionEventService transactionEventService;

    // Yeni transaction event yaratmaq
    @PostMapping
    public ResponseEntity<TransactionEventDto> createTransaction(@RequestBody TransactionEventDto transactionEventDto) {
        TransactionEventDto createdTransaction = transactionEventService.createTransactionEvent(transactionEventDto);
        return ResponseEntity.ok(createdTransaction);
    }

    // Bütün transaction event-ləri əldə etmək
    @GetMapping
    public ResponseEntity<List<TransactionEventDto>> getAllTransactions() {
        List<TransactionEventDto> transactions = transactionEventService.getAllTransactionEvents();
        return ResponseEntity.ok(transactions);
    }

    // ID-yə görə transaction event əldə etmək
    @GetMapping("/{id}")
    public ResponseEntity<TransactionEventDto> getTransactionById(@PathVariable Long id) {
        TransactionEventDto transaction = transactionEventService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
}
