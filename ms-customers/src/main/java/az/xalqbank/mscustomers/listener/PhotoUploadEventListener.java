package az.xalqbank.mscustomers.listener;

import az.xalqbank.mscustomers.config.RabbitMQConfig;
import az.xalqbank.mscustomers.service.CustomerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhotoUploadEventListener {

    private final CustomerService customerService; // Service inject edilir ✅

    @RabbitListener(queues = RabbitMQConfig.PHOTO_UPLOAD_QUEUE)
    public void handlePhotoUploadEvent(String message) {
        System.out.println("🔵 [ms-customer] Received message from RabbitMQ: " + message);

        try {
            // Mesaj formatı: "Photo uploaded for customer ID: 1, File URL: http://..."
            String[] messageParts = message.split(", ");
            String customerIdPart = messageParts[0];
            String fileUrlPart   = messageParts[1];

            Long customerId = Long.parseLong(customerIdPart.split(": ")[1]);
            String fileUrl  = fileUrlPart.split(": ")[1];

            // CustomerService üzərindən update çağırırıq
            customerService.updateCustomerProfilePhoto(customerId, fileUrl);

            System.out.println("✅ [ms-customer] Updated profile photo for customer ID: " + customerId);
        } catch (Exception e) {
            System.err.println("❌ [ms-customer] Error processing message: " + e.getMessage());
        }
    }
}
