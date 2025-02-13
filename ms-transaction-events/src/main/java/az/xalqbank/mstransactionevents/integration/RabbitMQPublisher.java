package az.xalqbank.mstransactionevents.integration;

import az.xalqbank.mstransactionevents.config.RabbitMQConfiguration;
import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ publisher service for sending transaction events.
 */
@Service
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publishes a transaction event to the configured exchange and routing key.
     *
     * @param event the TransactionEventDto to publish.
     */
    public void publishTransactionEvent(TransactionEventDto event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.EXCHANGE_NAME,
                RabbitMQConfiguration.ROUTING_KEY,
                event
        );
    }
}
