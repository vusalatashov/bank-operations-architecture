package az.xalqbank.msfileupload.service;

import az.xalqbank.msfileupload.domain.model.Photo;
import az.xalqbank.msfileupload.domain.repository.PhotoRepository;
import az.xalqbank.msfileupload.dto.PhotoDTO;
import az.xalqbank.msfileupload.event.PhotoUploadedEvent;
import az.xalqbank.msfileupload.exception.PhotoStorageException;
import az.xalqbank.msfileupload.mapper.PhotoMapper;
import az.xalqbank.msfileupload.publisher.PhotoUploadEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)  // Lenient strictness istifadə edilir
class PhotoServiceImplTest {

    @InjectMocks
    private PhotoServiceImpl photoService;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private PhotoMapper photoMapper;

    @Mock
    private PhotoUploadEventPublisher publisher;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private CustomerServiceClient customerServiceClient;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private MultipartFile file;

    private Photo photo;
    private PhotoDTO photoDTO;

    @BeforeEach
    void setUp() {
        photo = new Photo();
        photo.setId(1L);
        photo.setCustomerId(2L);
        photo.setFileName("test.jpg");
        photo.setFileUrl("/photos/test.jpg");

        photoDTO = new PhotoDTO(1L, 2L, "test.jpg", "/photos/test.jpg");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);  // Bu stub-lar hələ də qalır
    }


    @Test
    void uploadPhoto_CustomerNotFound() {
        when(customerServiceClient.isCustomerExists(2L)).thenReturn(false);

        Exception exception = assertThrows(PhotoStorageException.class, () -> {
            photoService.uploadPhoto(2L, file);
        });

        assertThat(exception.getMessage()).isEqualTo("Customer with ID 2 not found.");
        verifyNoInteractions(photoRepository, publisher, applicationEventPublisher);
    }

    @Test
    void getPhotoById_FoundInCache() {
        when(valueOperations.get("photo:1")).thenReturn(photoDTO);

        PhotoDTO result = photoService.getPhotoById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFileName()).isEqualTo("test.jpg");
        verify(photoRepository, never()).findById(anyLong());
    }

    @Test
    void getPhotoById_NotFoundInCache_ButExistsInDB() {
        when(valueOperations.get("photo:1")).thenReturn(null);
        when(photoRepository.findById(1L)).thenReturn(Optional.of(photo));
        when(photoMapper.toDto(photo)).thenReturn(photoDTO);

        PhotoDTO result = photoService.getPhotoById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFileName()).isEqualTo("test.jpg");
        verify(valueOperations).set("photo:1", photoDTO, Duration.ofMinutes(10));
    }

    @Test
    void getPhotoById_NotFound() {
        when(valueOperations.get("photo:1")).thenReturn(null);
        when(photoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PhotoStorageException.class, () -> {
            photoService.getPhotoById(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Photo with ID 1 not found.");
    }

    @Test
    void deletePhoto_Success() {
        when(photoRepository.existsById(1L)).thenReturn(true);

        photoService.deletePhoto(1L);

        verify(photoRepository).deleteById(1L);
        verify(redisTemplate).delete("photo:1");
    }

    @Test
    void deletePhoto_NotFound() {
        when(photoRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(PhotoStorageException.class, () -> {
            photoService.deletePhoto(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Photo with ID 1 not found.");
    }

    @Test
    void listPhotos_Success() {
        when(photoRepository.findAll()).thenReturn(List.of(photo));
        when(photoMapper.toDto(photo)).thenReturn(photoDTO);

        List<PhotoDTO> result = photoService.listPhotos();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getFileName()).isEqualTo("test.jpg");
    }
}
