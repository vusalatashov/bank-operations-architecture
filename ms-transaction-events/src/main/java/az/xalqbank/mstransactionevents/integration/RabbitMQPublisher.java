package az.xalqbank.mstransactionevents.integration;

import az.xalqbank.mstransactionevents.config.RabbitMQConfiguration;
import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishTransactionEvent(TransactionEventDto event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.EXCHANGE_NAME,
                RabbitMQConfiguration.ROUTING_KEY,
                event
        );
    }
}
