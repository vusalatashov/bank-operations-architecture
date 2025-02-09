package az.xalqbank.mscustomers.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration class that sets up the photo upload queue.
 */
@Configuration
public class RabbitMQConfig {

    public static final String PHOTO_UPLOAD_QUEUE = "photoUploadQueue";

    /**
     * Bean definition for the photo upload queue.
     *
     * @return Queue instance configured as durable.
     */
    @Bean
    public Queue photoUploadQueue() {
        return new Queue(PHOTO_UPLOAD_QUEUE, true); // Durable queue
    }
}
