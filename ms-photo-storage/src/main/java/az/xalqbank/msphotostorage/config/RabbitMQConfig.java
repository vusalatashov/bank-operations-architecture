package az.xalqbank.msphotostorage.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // ms-customer tarafında da PHOTO_UPLOAD_QUEUE olarak tanımladığınız isimle aynı olmalı:
    public static final String PHOTO_UPLOAD_QUEUE = "photoUploadQueue";

    @Bean
    public Queue photoUploadQueue() {
        return new Queue(PHOTO_UPLOAD_QUEUE, true); // Kuyruğu durable (kalıcı) olarak tanımlıyoruz
    }
    @Bean
    public Queue deletePhotoQueue() {
        return new Queue("deletePhotoQueue", true); // Durable (kalıcı) queue
    }

}