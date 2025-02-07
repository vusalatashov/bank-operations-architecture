package az.xalqbank.msphotostorage.mapper;

import az.xalqbank.msphotostorage.dto.PhotoDTO;
import az.xalqbank.msphotostorage.model.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(target = "id", ignore = true)  // Gereksiz id'yi yok sayıyoruz
    @Mapping(target = "fileName", source = "photo.fileName")  // Direkt olarak dönüşüm
    @Mapping(target = "fileUrl", source = "photo.fileUrl")  // Dosya URL'sini alıyoruz
    PhotoDTO toDto(Photo photo);  // Photo'dan PhotoDTO'ya dönüşüm

    @Mapping(target = "customerId", source = "photoDTO.customerId")
    @Mapping(target = "fileName", source = "photoDTO.fileName")
    @Mapping(target = "fileUrl", source = "photoDTO.fileUrl")
    Photo toEntity(PhotoDTO photoDTO);  // DTO'dan Entity'ye dönüşüm
}
