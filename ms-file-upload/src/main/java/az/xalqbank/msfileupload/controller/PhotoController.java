package az.xalqbank.msfileupload.controller;

import az.xalqbank.msfileupload.dto.PhotoDTO;
import az.xalqbank.msfileupload.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of PhotoController providing REST endpoints for photo operations.
 */
@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    /**
     * {@inheritDoc}
     */

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<PhotoDTO> uploadPhoto(@RequestParam("customerId") Long customerId,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        PhotoDTO savedPhotoDTO = photoService.uploadPhoto(customerId, file);
        return new ResponseEntity<>(savedPhotoDTO, HttpStatus.CREATED);
    }

    /**
     * {@inheritDoc}
     */

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable Long photoId) {
        PhotoDTO photoDTO = photoService.getPhotoById(photoId);
        return ResponseEntity.ok(photoDTO);
    }

    /**
     * {@inheritDoc}
     */
    @DeleteMapping("/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.ok("Photo deleted successfully.");
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping()
    public ResponseEntity<List<PhotoDTO>> listPhotos() {
        List<PhotoDTO> photos = photoService.listPhotos();
        return ResponseEntity.ok(photos);
    }
}
