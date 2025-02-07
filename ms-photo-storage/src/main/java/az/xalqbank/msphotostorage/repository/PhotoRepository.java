package az.xalqbank.msphotostorage.repository;

import az.xalqbank.msphotostorage.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {
    // İhtiyaç halinde ek sorgu metodları eklenebilir.
}
