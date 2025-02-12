package az.xalqbank.msfileupload.config;

import az.xalqbank.msfileupload.mapper.PhotoMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for MapStruct mappers.
 */
@Configuration
public class MapperConfig {

    @Bean
    public PhotoMapper photoMapper() {
        return Mappers.getMapper(PhotoMapper.class);
    }
}
