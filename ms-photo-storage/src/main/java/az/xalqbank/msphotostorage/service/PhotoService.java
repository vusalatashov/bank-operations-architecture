package az.xalqbank.msphotostorage.service;

import az.xalqbank.msphotostorage.dto.PhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {

    /**
     * Müşteri ID'si ve dosya bilgisini alarak fotoğrafı yükler, DB'ye kaydeder,
     * RabbitMQ'ya event yayınlar, PhotoDTO döndürür.
     */
    PhotoDTO uploadPhoto(Long customerId, MultipartFile file) throws IOException;

    /**
     * Fotoğrafı ID ile bulup DTO döndürür (Önbellekten ya da DB'den).
     */
    PhotoDTO getPhotoById(Long photoId);
}