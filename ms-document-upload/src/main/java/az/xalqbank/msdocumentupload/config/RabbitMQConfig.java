package az.xalqbank.msdocumentupload.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ.
 */
@Configuration
public class RabbitMQConfig {

    // Define the exchange and routing key names as needed.
    public static final String DOCUMENT_UPLOAD_QUEUE = "document.upload.queue";

    /**
     * Defines a durable queue for document upload events.
     *
     * @return the RabbitMQ Queue.
     */
    @Bean
    public Queue documentUploadQueue() {
        return new Queue(DOCUMENT_UPLOAD_QUEUE, true); // durable queue
    }
}
