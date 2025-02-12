package az.xalqbank.msfileupload.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listener for photo-related events from RabbitMQ.
 */
@Component
public class PhotoEventListener {

    /**
     * Listens for photo deletion events.
     *
     * @param message the received message.
     */
    @RabbitListener(queues = "deletePhotoQueue")
    public void handleDeletePhotoEvent(String message) {
        System.out.println("Received message to delete photo: " + message);
        // Implement additional deletion logic if needed.
    }
}
