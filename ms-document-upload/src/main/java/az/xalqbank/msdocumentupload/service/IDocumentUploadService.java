package az.xalqbank.msdocumentupload.service;
import az.xalqbank.msdocumentupload.dto.DocumentUploadDto;

/**
 * Interface for the document upload service.
 * Defines the contract for document upload operations.
 */
public interface IDocumentUploadService {

    /**
     * Uploads a document.
     *
     * @param documentUploadDto the document upload details.
     */
    void uploadDocument(DocumentUploadDto documentUploadDto);
}
