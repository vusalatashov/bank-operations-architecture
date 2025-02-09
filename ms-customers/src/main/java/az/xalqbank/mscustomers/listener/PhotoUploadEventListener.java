package az.xalqbank.mscustomers.listener;

import az.xalqbank.mscustomers.config.RabbitMQConfig;
import az.xalqbank.mscustomers.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhotoUploadEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PhotoUploadEventListener.class);

    private final CustomerService customerService;

    /**
     * Listens for messages from the PHOTO_UPLOAD_QUEUE.
     *
     * @param message The message containing the customer ID and file URL.
     */
    @RabbitListener(queues = RabbitMQConfig.PHOTO_UPLOAD_QUEUE)
    public void handlePhotoUploadEvent(String message) {
        logger.info("Received message from RabbitMQ: {}", message);

        try {
            // Parsing the incoming message into parts: "Photo uploaded for customer ID: 1, File URL: http://..."
            String[] messageParts = message.split(", ");
            if (messageParts.length != 2) {
                logger.warn("Invalid message format, expected 'customer ID' and 'file URL'. Message: {}", message);
                return;
            }

            String customerIdPart = messageParts[0];
            String fileUrlPart = messageParts[1];

            // Extracting customer ID and file URL from the message parts
            Long customerId = parseCustomerId(customerIdPart);
            String fileUrl = parseFileUrl(fileUrlPart);

            if (customerId == null || fileUrl == null) {
                logger.warn("Failed to extract valid customerId or fileUrl from message. Skipping processing. Message: {}", message);
                return;
            }

            // Update profile photo for the customer using the service
            customerService.updateCustomerProfilePhoto(customerId, fileUrl);

            logger.info("Successfully updated profile photo for customer ID: {}", customerId);
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }
    }

    /**
     * Parses the customer ID from the message part.
     *
     * @param customerIdPart The message part containing the customer ID.
     * @return Parsed customer ID, or null if parsing fails.
     */
    private Long parseCustomerId(String customerIdPart) {
        try {
            return Long.parseLong(customerIdPart.split(": ")[1]);
        } catch (Exception e) {
            logger.error("Failed to parse customer ID from part: {}", customerIdPart, e);
            return null;
        }
    }

    /**
     * Parses the file URL from the message part.
     *
     * @param fileUrlPart The message part containing the file URL.
     * @return Parsed file URL, or null if parsing fails.
     */
    private String parseFileUrl(String fileUrlPart) {
        try {
            return fileUrlPart.split(": ")[1];
        } catch (Exception e) {
            logger.error("Failed to parse file URL from part: {}", fileUrlPart, e);
            return null;
        }
    }
}
