package az.xalqbank.mstransactionevents.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * RabbitMQ configuration class for setting up queue, exchange, and bindings.
 */
@Configuration
public class RabbitMQConfiguration {

    public static final String QUEUE_NAME = "transaction_events_queue";
    public static final String EXCHANGE_NAME = "transaction_events_exchange";
    public static final String ROUTING_KEY = "transaction.events";

    /**
     * Creates a durable queue for transaction events.
     *
     * @return the configured Queue.
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Creates a direct exchange for transaction events.
     *
     * @return the configured DirectExchange.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * Binds the queue to the exchange with a routing key.
     *
     * @param queue    the transaction events queue.
     * @param exchange the direct exchange.
     * @return the configured Binding.
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * Configures a Jackson2JsonMessageConverter with a custom type mapper.
     *
     * @return the configured Jackson2JsonMessageConverter.
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.setIdClassMapping(Map.of(
                "az.xalqbank.mstransactionevents.dto.TransactionEventDto",
                az.xalqbank.mstransactionevents.dto.TransactionEventDto.class
        ));

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    /**
     * Creates a RabbitListenerContainerFactory with the custom message converter.
     *
     * @param connectionFactory the connection factory.
     * @param converter         the Jackson2JsonMessageConverter.
     * @return the configured RabbitListenerContainerFactory.
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }
}
