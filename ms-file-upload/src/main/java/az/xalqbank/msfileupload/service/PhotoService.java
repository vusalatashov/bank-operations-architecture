package az.xalqbank.msfileupload.service;

import az.xalqbank.msfileupload.dto.PhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service interface for photo operations.
 */
public interface PhotoService {

    /**
     * Uploads a photo.
     *
     * @param customerId the customer ID.
     * @param file       the photo file.
     * @return the uploaded photo data.
     * @throws IOException if file handling fails.
     */
    PhotoDTO uploadPhoto(Long customerId, MultipartFile file) throws IOException;

    /**
     * Retrieves a photo by ID.
     *
     * @param photoId the photo ID.
     * @return the photo data.
     */
    PhotoDTO getPhotoById(Long photoId);

    /**
     * Deletes a photo by ID.
     *
     * @param photoId the photo ID.
     */
    void deletePhoto(Long photoId);

    /**
     * Lists all photos.
     *
     * @return a list of photo DTOs.
     */
    List<PhotoDTO> listPhotos();
}
