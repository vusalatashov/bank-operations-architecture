package az.xalqbank.msphotostorage.controller;

import az.xalqbank.msphotostorage.dto.PhotoDTO;
import az.xalqbank.msphotostorage.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoControllerImpl implements PhotoController {

    private final PhotoService photoService;

    /**
     * Fotoğrafı alır, kaydeder, RabbitMQ'ya event gönderir.
     */
    @Override
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<PhotoDTO> uploadPhoto(@RequestParam("customerId") Long customerId,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        PhotoDTO savedPhotoDTO = photoService.uploadPhoto(customerId, file);
        return new ResponseEntity<>(savedPhotoDTO, HttpStatus.CREATED);
    }

    /**
     * Fotoğraf meta bilgilerini ID ile döndürür (ör. DB'deki kaydı).
     */
    @Override
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable Long photoId) {
        PhotoDTO photoDTO = photoService.getPhotoById(photoId);
        if (photoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photoDTO);
    }

    // İsteğe bağlı: Dosyayı gerçekten döndürmek (indirtmek) için bir endpoint eklenebilir.
    /*
    @GetMapping("/{photoId}/download")
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable Long photoId) {
        // Implementation...
    }
    */
}