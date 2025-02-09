package az.xalqbank.msdocumentupload.service;

import az.xalqbank.msdocumentupload.Integrations.FileStorageService;
import az.xalqbank.msdocumentupload.Integrations.RedisCacheService;
import az.xalqbank.msdocumentupload.dto.DocumentUploadDto;
import az.xalqbank.msdocumentupload.mapper.DocumentMapper;
import az.xalqbank.msdocumentupload.model.LoanDocument;
import az.xalqbank.msdocumentupload.repository.DocumentRepository;
import az.xalqbank.msdocumentupload.util.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Service for handling the document upload process.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentUploadService implements IDocumentUploadService {

    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final FileValidator fileValidator;
    private final RedisCacheService redisCacheService;

    /**
     * Handles the document upload process:
     * 1. Validates the file.
     * 2. Stores the file externally and retrieves its URL.
     * 3. Maps the DTO to a LoanDocument entity and saves metadata to the database.
     * 4. Caches the document metadata in Redis.
     *
     * @param documentUploadDto the document upload DTO.
     */
    @Override
    public void uploadDocument(DocumentUploadDto documentUploadDto) {
        log.info("Starting document upload for customer: {}", documentUploadDto.getCustomerId());

        // Validate the file using FileValidator
        boolean valid = fileValidator.validateFile(documentUploadDto.getDocumentFile(), documentUploadDto.getDocumentSize());
        if (!valid) {
            log.error("Document validation failed for customer: {}", documentUploadDto.getCustomerId());
            throw new IllegalArgumentException("Invalid file format or size.");
        }

        // Store the file using the FileStorageService and retrieve the file URL
        String fileUrl = fileStorageService.storeFile(
                documentUploadDto.getDocumentFile(),
                documentUploadDto.getDocumentType() + "_" + documentUploadDto.getCustomerId()
        );
        log.info("File stored successfully at URL: {}", fileUrl);

        // Map the DTO to the LoanDocument entity using DocumentMapper
        LoanDocument loanDocument = DocumentMapper.mapToEntity(documentUploadDto);

        // Set the file URL in the LoanDocument entity (only the URL is stored)
        loanDocument.setDocumentUrl(fileUrl);

        // Save the document metadata (including file URL) to the database
        LoanDocument savedDocument = documentRepository.save(loanDocument);
        log.info("Document saved in the database with id: {}", savedDocument.getId());

        // Cache the document metadata in Redis
        String cacheKey = "document:" + savedDocument.getId();
        redisCacheService.cacheData(cacheKey, savedDocument, Duration.ofMinutes(10));
        log.info("Document cached with key: {}", cacheKey);
    }
}
