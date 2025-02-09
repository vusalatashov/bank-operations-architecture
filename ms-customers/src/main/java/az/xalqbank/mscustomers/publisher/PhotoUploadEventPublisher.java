package az.xalqbank.mscustomers.publisher;

import az.xalqbank.mscustomers.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PhotoUploadEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(PhotoUploadEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public PhotoUploadEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * Publish a message to RabbitMQ to notify that a photo should be deleted for a customer.
     *
     * @param customerId ID of the customer
     */
    public void publishDeletePhotoEvent(Long customerId) {
        String message = String.format("Delete photo for customer ID: %d", customerId);

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.PHOTO_UPLOAD_QUEUE, message);
            logger.info("Successfully sent photo delete event for customer ID: {}", customerId);
        } catch (Exception e) {
            logger.error("Failed to send photo delete event for customer ID: {}. Error: {}", customerId, e.getMessage());
        }
    }
}
