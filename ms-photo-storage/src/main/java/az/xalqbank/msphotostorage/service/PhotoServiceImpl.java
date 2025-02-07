package az.xalqbank.msphotostorage.service;

import az.xalqbank.msphotostorage.dto.PhotoDTO;
import az.xalqbank.msphotostorage.mapper.PhotoMapper;
import az.xalqbank.msphotostorage.model.Photo;
import az.xalqbank.msphotostorage.publisher.PhotoUploadEventPublisher;
import az.xalqbank.msphotostorage.repository.PhotoRepository;
import az.xalqbank.msphotostorage.util.FileStorageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final PhotoUploadEventPublisher publisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomerServiceClient customerServiceClient; // CustomerServiceClient əlavə olunur

    @Override
    public PhotoDTO uploadPhoto(Long customerId, MultipartFile file) throws IOException {
        // Müştəri yoxlanır
        if (!customerServiceClient.isCustomerExists(customerId)) {
            throw new RuntimeException("Customer with ID " + customerId + " not found.");
        }

        // 1) Dosyayı diske ya da harici bir storage'a kaydet
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            // Basit bir kontrol
            fileName = "unknown-file";
        }

        // Dosyayı saklayıp URL dönen yardımcı metodumuz (örnek olarak yerel disk):
        String fileUrl = FileStorageUtils.saveFileToLocal(file);

        // 2) DB'ye kaydetmek için Photo nesnesi oluştur
        Photo photo = new Photo();
        photo.setCustomerId(customerId);
        photo.setFileName(fileName);
        photo.setFileUrl(fileUrl);

        Photo savedPhoto = photoRepository.save(photo);
        PhotoDTO dto = photoMapper.toDto(savedPhoto);

        // 3) RabbitMQ'ya mesaj gönder => ms-customer bu mesajı dinleyip kendi profil kaydını güncelleyecek
        publisher.publishPhotoUploadEvent(customerId, fileUrl);

        // 4) Redis cache'e ekle (örnek olarak "photo:{id}" key'i ile)
        String cacheKey = "photo:" + savedPhoto.getId();
        redisTemplate.opsForValue().set(cacheKey, dto, Duration.ofMinutes(10));

        return dto;
    }

    @Override
    public PhotoDTO getPhotoById(Long photoId) {
        String cacheKey = "photo:" + photoId;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // Redis cache-dən alınan məlumatları deserializasiya edirik
        PhotoDTO cachedPhoto = (PhotoDTO) ops.get(cacheKey);
        if (cachedPhoto != null) {
            return cachedPhoto;
        }

        // Redis cache-də yoxdursa, DB-dən çəkirik
        Optional<Photo> optionalPhoto = photoRepository.findById(String.valueOf(photoId));
        if (optionalPhoto.isEmpty()) {
            return null;
        }

        PhotoDTO dto = photoMapper.toDto(optionalPhoto.get());

        // Redis cache-ə əlavə edirik
        ops.set(cacheKey, dto, Duration.ofMinutes(10));

        return dto;
    }
}
