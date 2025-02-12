package az.xalqbank.msfileupload.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for queues.
 */
@Configuration
public class RabbitMQConfig {

    // The queue names must be consistent across microservices.
    public static final String PHOTO_UPLOAD_QUEUE = "photoUploadQueue";
    public static final String PHOTO_DELETE_QUEUE = "deletePhotoQueue";

    @Bean
    public Queue photoUploadQueue() {
        return new Queue(PHOTO_UPLOAD_QUEUE, true); // Durable queue
    }

    @Bean
    public Queue photoDeleteQueue() {
        return new Queue(PHOTO_DELETE_QUEUE, true); // Durable queue
    }
}
