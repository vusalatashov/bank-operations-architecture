package az.xalqbank.mstransactionevents.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
