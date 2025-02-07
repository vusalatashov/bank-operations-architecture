package az.xalqbank.msphotostorage.repository;

import az.xalqbank.msphotostorage.model.PhotoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoMetadataRepository extends JpaRepository<PhotoMetadata, String> {
    // İhtiyaç halinde ek sorgu metodları eklenebilir.
}
