package az.xalqbank.msphotostorage.controller;

import az.xalqbank.msphotostorage.dto.PhotoResponse;
import az.xalqbank.msphotostorage.dto.PhotoUploadRequest;
import az.xalqbank.msphotostorage.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    public ResponseEntity<PhotoResponse> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("name") String name,
                                                     @RequestParam("contactDetails") String contactDetails) {
        // DTO oluşturuluyor.
        PhotoUploadRequest request = PhotoUploadRequest.builder()
                .file(file)
                .name(name)
                .contactDetails(contactDetails)
                .build();

        PhotoResponse response = photoService.uploadPhoto(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
