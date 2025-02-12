package az.xalqbank.msfileupload.service;

import az.xalqbank.msfileupload.dto.PhotoDTO;
import az.xalqbank.msfileupload.event.PhotoUploadedEvent;
import az.xalqbank.msfileupload.exception.PhotoStorageException;
import az.xalqbank.msfileupload.mapper.PhotoMapper;
import az.xalqbank.msfileupload.domain.model.Photo;
import az.xalqbank.msfileupload.publisher.PhotoUploadEventPublisher;
import az.xalqbank.msfileupload.domain.repository.PhotoRepository;
import az.xalqbank.msfileupload.util.FileStorageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of PhotoService.
 */
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final PhotoUploadEventPublisher publisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomerServiceClient customerServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public PhotoDTO uploadPhoto(Long customerId, MultipartFile file) throws IOException {
        // Verify if customer exists
        if (!customerServiceClient.isCustomerExists(customerId)) {
            throw new PhotoStorageException("Customer with ID " + customerId + " not found.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            fileName = "unknown-file";
        }

        // Save file locally
        String fileUrl;
        try {
            fileUrl = FileStorageUtils.saveFileToLocal(file);
        } catch (IOException e) {
            throw new PhotoStorageException("Failed to store file.", e);
        }

        // Create photo entity and save to database
        Photo photo = new Photo();
        photo.setCustomerId(customerId);
        photo.setFileName(fileName);
        photo.setFileUrl(fileUrl);
        Photo savedPhoto = photoRepository.save(photo);
        PhotoDTO photoDTO = photoMapper.toDto(savedPhoto);

        // Publish event to RabbitMQ
        publisher.publishPhotoUploadEvent(customerId, fileUrl);

        // Yenisə, PhotoUploadedEvent-i yayımlayırıq:
        applicationEventPublisher.publishEvent(new PhotoUploadedEvent(this, savedPhoto));

        // Cache the photo in Redis with a TTL of 10 minutes
        String cacheKey = "photo:" + savedPhoto.getId();
        redisTemplate.opsForValue().set(cacheKey, photoDTO, Duration.ofMinutes(10));

        return photoDTO;
    }
    @Override
    public PhotoDTO getPhotoById(Long photoId) {
        String cacheKey = "photo:" + photoId;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // Attempt to retrieve from cache
        PhotoDTO cachedPhoto = (PhotoDTO) ops.get(cacheKey);
        if (cachedPhoto != null) {
            return cachedPhoto;
        }

        // Retrieve from database
        Optional<Photo> optionalPhoto = photoRepository.findById(photoId);
        if (optionalPhoto.isEmpty()) {
            throw new PhotoStorageException("Photo with ID " + photoId + " not found.");
        }
        PhotoDTO photoDTO = photoMapper.toDto(optionalPhoto.get());

        // Cache the result
        ops.set(cacheKey, photoDTO, Duration.ofMinutes(10));
        return photoDTO;
    }

    @Override
    public void deletePhoto(Long photoId) {
        if (!photoRepository.existsById(photoId)) {
            throw new PhotoStorageException("Photo with ID " + photoId + " not found.");
        }
        photoRepository.deleteById(photoId);
        String cacheKey = "photo:" + photoId;
        redisTemplate.delete(cacheKey);
    }

    @Override
    public List<PhotoDTO> listPhotos() {
        List<Photo> photos = photoRepository.findAll();
        return photos.stream().map(photoMapper::toDto).collect(Collectors.toList());
    }
}
