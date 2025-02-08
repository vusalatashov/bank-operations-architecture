package az.xalqbank.mstransactionevents.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    private String transactionType; // deposit, withdrawal, transfer

    private Double amount;

    private LocalDateTime transactionDate;

    private String status; // success, failed, pending
}
