package az.xalqbank.mscustomers.mapper;

import az.xalqbank.mscustomers.dto.ProfilePhotoDTO;
import az.xalqbank.mscustomers.model.ProfilePhoto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfilePhotoMapper {

    ProfilePhotoMapper INSTANCE = Mappers.getMapper(ProfilePhotoMapper.class);

    // ProfilePhoto Entity'den ProfilePhotoDTO'ya dönüşüm
    ProfilePhotoDTO toDTO(ProfilePhoto profilePhoto);

    // ProfilePhotoDTO'dan ProfilePhoto Entity'ye dönüşüm
    ProfilePhoto toEntity(ProfilePhotoDTO profilePhotoDTO);
}
