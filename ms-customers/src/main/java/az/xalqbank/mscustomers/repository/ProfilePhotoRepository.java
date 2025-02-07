package az.xalqbank.mscustomers.repository;

import az.xalqbank.mscustomers.model.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// In ProfilePhotoRepository.java
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {
}

