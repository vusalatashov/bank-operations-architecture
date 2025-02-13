package az.xalqbank.mstransactionevents.integration;

import az.xalqbank.mstransactionevents.config.RabbitMQConfiguration;
import az.xalqbank.mstransactionevents.dto.TransactionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ listener service for receiving transaction events.
 */
@Slf4j
@Service
public class RabbitMQListener {

    /**
     * Receives and logs transaction events from the queue.
     *
     * @param event the received TransactionEventDto.
     */
    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_NAME)
    public void receiveTransactionEvent(TransactionEventDto event) {
        log.info("Received Transaction Event: {}", event);
    }
}
