package az.xalqbank.mstransactionevents.config;

import az.xalqbank.mstransactionevents.mapper.TransactionEventMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public TransactionEventMapper transactionEventMapper() {
        return Mappers.getMapper(TransactionEventMapper.class);
    }
}
