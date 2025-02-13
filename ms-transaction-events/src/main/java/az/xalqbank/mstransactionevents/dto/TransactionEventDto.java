package az.xalqbank.mstransactionevents.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for TransactionEvent.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventDto implements Serializable {
    private Long customerId;
    private String transactionType;
    private Double amount;
    private LocalDateTime transactionDate;
    private String status;
}
