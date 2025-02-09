package az.xalqbank.msdocumentupload.config;

import az.xalqbank.msdocumentupload.Integrations.FileStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for storage-related beans.
 */
@Configuration
public class StorageConfiguration {

    @Bean
    public FileStorageService fileStorageService() {
        return new FileStorageService();
    }
}
