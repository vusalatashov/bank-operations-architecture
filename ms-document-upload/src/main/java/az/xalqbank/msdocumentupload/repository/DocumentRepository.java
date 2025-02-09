package az.xalqbank.msdocumentupload.repository;

import az.xalqbank.msdocumentupload.model.LoanDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for LoanDocument entities.
 * Provides CRUD operations for loan documents.
 */
@Repository
public interface DocumentRepository extends CrudRepository<LoanDocument, Long> {
    // Additional query methods can be defined here if needed.
}
