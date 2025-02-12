package az.xalqbank.msfileupload.mapper;

import az.xalqbank.msfileupload.dto.PhotoDTO;
import az.xalqbank.msfileupload.domain.model.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting Photo entity to PhotoDTO and vice versa.
 */
@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(target = "fileName", source = "photo.fileName")
    @Mapping(target = "fileUrl", source = "photo.fileUrl")
    PhotoDTO toDto(Photo photo);

    @Mapping(target = "customerId", source = "photoDTO.customerId")
    @Mapping(target = "fileName", source = "photoDTO.fileName")
    @Mapping(target = "fileUrl", source = "photoDTO.fileUrl")
    Photo toEntity(PhotoDTO photoDTO);
}
