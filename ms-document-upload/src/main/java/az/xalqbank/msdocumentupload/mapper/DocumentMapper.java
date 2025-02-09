package az.xalqbank.msdocumentupload.mapper;

import az.xalqbank.msdocumentupload.dto.DocumentUploadDto;
import az.xalqbank.msdocumentupload.model.LoanDocument;

public class DocumentMapper {

    /**
     * Maps a DocumentUploadDto to a LoanDocument entity.
     *
     * @param documentUploadDto the DTO to map.
     * @return the mapped LoanDocument entity.
     */
    public static LoanDocument mapToEntity(DocumentUploadDto documentUploadDto) {
        LoanDocument loanDocument = new LoanDocument();
        loanDocument.setCustomerId(documentUploadDto.getCustomerId());
        loanDocument.setDocumentName(documentUploadDto.getDocumentName());
        loanDocument.setDocumentType(documentUploadDto.getDocumentType());
        loanDocument.setDocumentSize(documentUploadDto.getDocumentSize());
        // Other mapping as required
        return loanDocument;
    }
}
