package az.xalqbank.msfileupload.controller;

import az.xalqbank.msfileupload.dto.PhotoDTO;
import az.xalqbank.msfileupload.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class PhotoControllerImplTest {

    @Mock
    private PhotoService photoService;

    @InjectMocks
    private PhotoController photoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadPhoto_Success() throws IOException {
        Long customerId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        PhotoDTO photoDTO = new PhotoDTO(1L, customerId, "test.jpg", "file-url");

        when(photoService.uploadPhoto(customerId, file)).thenReturn(photoDTO);

        ResponseEntity<PhotoDTO> response = photoController.uploadPhoto(customerId, file);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(photoDTO, response.getBody());
    }

    @Test
    void getPhotoById_Success() {
        Long photoId = 1L;
        PhotoDTO photoDTO = new PhotoDTO(photoId, 1L, "test.jpg", "file-url");

        when(photoService.getPhotoById(photoId)).thenReturn(photoDTO);

        ResponseEntity<PhotoDTO> response = photoController.getPhotoById(photoId);

        assertEquals(OK, response.getStatusCode());
        assertEquals(photoDTO, response.getBody());
    }

    @Test
    void deletePhoto_Success() {
        Long photoId = 1L;
        doNothing().when(photoService).deletePhoto(photoId);

        ResponseEntity<String> response = photoController.deletePhoto(photoId);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Photo deleted successfully.", response.getBody());
    }

    @Test
    void listPhotos_Success() {
        PhotoDTO photoDTO = new PhotoDTO(1L, 1L, "test.jpg", "file-url");
        List<PhotoDTO> photoList = Collections.singletonList(photoDTO);

        when(photoService.listPhotos()).thenReturn(photoList);

        ResponseEntity<List<PhotoDTO>> response = photoController.listPhotos();

        assertEquals(OK, response.getStatusCode());
        assertEquals(photoList, response.getBody());
    }
}
