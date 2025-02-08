package az.xalqbank.mstransactionevents.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventDto {

    private String customerId;

    private String transactionType;

    private Double amount;

    private LocalDateTime transactionDate;

    private String status;
}
