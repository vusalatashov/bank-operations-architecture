package az.xalqbank.msfileupload.domain.repository;

import az.xalqbank.msfileupload.domain.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Photo entities.
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
