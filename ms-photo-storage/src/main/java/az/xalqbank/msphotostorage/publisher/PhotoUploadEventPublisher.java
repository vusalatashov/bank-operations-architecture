package az.xalqbank.msphotostorage.publisher;

import az.xalqbank.msphotostorage.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * ms-customer mikroservisinin dinlediği kuyruk olan 'photoUploadQueue'ya mesaj gönderir.
 */
@Component
public class PhotoUploadEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PhotoUploadEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPhotoUploadEvent(Long customerId, String fileUrl) {
        String message = "Photo uploaded for customer ID: " + customerId + ", File URL: " + fileUrl;

        System.out.println("📤 [ms-photo-storage] Sending message to RabbitMQ: " + message); // Log əlavə edildi

        rabbitTemplate.convertAndSend(RabbitMQConfig.PHOTO_UPLOAD_QUEUE, message);

        System.out.println("✅ [ms-photo-storage] Message sent successfully!");
    }
}
