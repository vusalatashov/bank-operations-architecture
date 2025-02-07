package az.xalqbank.mscustomers.publisher;

import az.xalqbank.mscustomers.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PhotoUploadEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PhotoUploadEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPhotoUploadEvent(Long customerId, String fileUrl) {
        String message = "Photo uploaded for customer ID: " + customerId + ", File URL: " + fileUrl;
        rabbitTemplate.convertAndSend(RabbitMQConfig.PHOTO_UPLOAD_QUEUE, message);
    }
    public void publishDeletePhotoEvent(Long customerId) {
        String message = "Delete photo for customer ID: " + customerId;
        rabbitTemplate.convertAndSend(RabbitMQConfig.PHOTO_UPLOAD_QUEUE, message);
    }

}