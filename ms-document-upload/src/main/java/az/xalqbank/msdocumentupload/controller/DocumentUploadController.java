package az.xalqbank.msdocumentupload.controller;

import az.xalqbank.msdocumentupload.dto.DocumentUploadDto;
import az.xalqbank.msdocumentupload.service.IDocumentUploadService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for handling document upload operations.
 * Exposes REST endpoints for uploading documents.
 * Publishes messages to RabbitMQ and logs important events.
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentUploadController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadController.class);

    private final IDocumentUploadService documentUploadService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Endpoint for uploading a document.
     *
     * @param documentFile the uploaded file.
     * @param customerId the customer ID.
     * @return ResponseEntity with a success message.
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadDocument(@RequestParam("document") MultipartFile documentFile,
                                                 @RequestParam("customerId") String customerId) {
        logger.info("Received document upload request for customer: {}", customerId);

        // Check if the file is empty
        if (documentFile.isEmpty()) {
            logger.error("No file uploaded for customer: {}", customerId);
            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
        }

        // Create a DTO and set the file and other fields
        DocumentUploadDto documentUploadDto = new DocumentUploadDto();
        documentUploadDto.setDocumentFile(documentFile);
        documentUploadDto.setCustomerId(customerId);
        documentUploadDto.setDocumentSize(documentFile.getSize());
        documentUploadDto.setDocumentName(documentFile.getOriginalFilename());
        documentUploadDto.setDocumentType(documentFile.getContentType());

        try {
            // Process the document upload (validate, store file, save metadata, etc.)
            logger.info("Processing document upload for customer: {}", customerId);
            documentUploadService.uploadDocument(documentUploadDto);

            // Publish a message to RabbitMQ for further processing (sending only metadata)
            rabbitTemplate.convertAndSend("documentExchange", "document.upload",
                    String.format("Document uploaded for customer %s, type %s", customerId, documentFile.getContentType()));
            logger.info("Document uploaded successfully and message published for customer: {}", customerId);

            return new ResponseEntity<>("Document uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error during document upload for customer: {}. Error: {}", customerId, e.getMessage());
            return new ResponseEntity<>("Error uploading document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
