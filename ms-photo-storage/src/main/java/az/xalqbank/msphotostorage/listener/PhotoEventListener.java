package az.xalqbank.msphotostorage.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PhotoEventListener {

    // Örnek, ms-customer "deletePhotoQueue" gibi bir kuyruktan fotoğrafın da silinmesi
    // gerektiğini ms-photo-storage'a bildirebilir. Kurgunuza göre tasarlayabilirsiniz.
    @RabbitListener(queues = "deletePhotoQueue")
    public void handleDeletePhotoEvent(String message) {
        System.out.println("Received message to delete photo: " + message);
        // parsing, DB'den silme vb...
    }
}