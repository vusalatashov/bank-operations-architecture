package az.xalqbank.msphotostorage.controller;

import az.xalqbank.msphotostorage.dto.PhotoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoController {

    /**
     * Bir müşteri için fotoğraf yükler.
     *
     * @param customerId  Müşteri ID'si
     * @param file        Fotoğraf dosyası (multipart)
     * @return Yüklenmiş fotoğrafın DTO'su
     */
    ResponseEntity<PhotoDTO> uploadPhoto(Long customerId, MultipartFile file) throws IOException;

    /**
     * ID'si verilen fotoğraf bilgilerini getirir.
     */
    ResponseEntity<PhotoDTO> getPhotoById(Long photoId);

    /**
     * Fotoğraf dosyasını gerçekten indirip döndürmek için (opsiyonel).
     * Burada ResponseEntity<byte[]> dönebilir veya bir redirect yapabilirsiniz.
     */
    // ResponseEntity<byte[]> downloadPhoto(Long photoId);
}