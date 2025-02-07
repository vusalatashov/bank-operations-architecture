package az.xalqbank.msphotostorage.service;

import az.xalqbank.msphotostorage.dto.PhotoResponse;
import az.xalqbank.msphotostorage.dto.PhotoUploadRequest;
import az.xalqbank.msphotostorage.event.PhotoUploadedEvent;
import az.xalqbank.msphotostorage.exception.PhotoStorageException;
import az.xalqbank.msphotostorage.model.PhotoMetadata;
import az.xalqbank.msphotostorage.repository.PhotoMetadataRepository;
import az.xalqbank.msphotostorage.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final StorageService storageService;
    private final PhotoMetadataRepository photoMetadataRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PhotoResponse uploadPhoto(PhotoUploadRequest request) {
        try {
            // Dosya ismine benzersiz bir isim oluşturuluyor.
            String fileName = UUID.randomUUID().toString() + "_" + request.getFile().getOriginalFilename();
            String fileUrl = storageService.store(request.getFile(), fileName);

            // Dosya metadata’sı çıkarılıyor.
            long fileSize = request.getFile().getSize();
            String fileType = FileUtils.getFileExtension(request.getFile().getOriginalFilename());

            // Metadata veritabanına kaydediliyor.
            PhotoMetadata metadata = PhotoMetadata.builder()
                    .id(UUID.randomUUID().toString())
                    .name(request.getName())
                    .contactDetails(request.getContactDetails())
                    .fileName(fileName)
                    .fileUrl(fileUrl)
                    .fileSize(fileSize)
                    .fileType(fileType)
                    .uploadTime(LocalDateTime.now())
                    .build();
            photoMetadataRepository.save(metadata);

            // Fotoğraf yüklendiğinde domain event yayınlanıyor.
            PhotoUploadedEvent event = new PhotoUploadedEvent(this, metadata);
            eventPublisher.publishEvent(event);

            return PhotoResponse.builder()
                    .id(metadata.getId())
                    .url(fileUrl)
                    .message("Photo uploaded successfully")
                    .build();

        } catch (IOException e) {
            throw new PhotoStorageException("Failed to store photo", e);
        }
    }
}
