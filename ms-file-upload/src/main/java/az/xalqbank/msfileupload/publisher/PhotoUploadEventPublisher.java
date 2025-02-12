package az.xalqbank.msfileupload.publisher;

import az.xalqbank.msfileupload.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes photo upload events to RabbitMQ.
 */
@Component
@RequiredArgsConstructor
public class PhotoUploadEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publishes an event indicating that a photo has been uploaded.
     *
     * @param customerId the ID of the customer.
     * @param fileUrl    the URL of the uploaded file.
     */
    public void publishPhotoUploadEvent(Long customerId, String fileUrl) {
        String message = "Photo uploaded for customer ID: " + customerId + ", File URL: " + fileUrl;
        System.out.println("Sending message to RabbitMQ: " + message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.PHOTO_UPLOAD_QUEUE, message);
        System.out.println("Message sent successfully!");
    }
}
