package az.xalqbank.mstransactionevents.config;

import az.xalqbank.mstransactionevents.mapper.TransactionEventMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mapper configuration class to initialize mappers.
 */
@Configuration
public class MapperConfig {

    /**
     * Creates a TransactionEventMapper bean using MapStruct.
     *
     * @return a TransactionEventMapper instance.
     */
    @Bean
    public TransactionEventMapper transactionEventMapper() {
        return Mappers.getMapper(TransactionEventMapper.class);
    }
}
