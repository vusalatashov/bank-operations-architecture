package az.xalqbank.mscustomers.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

@Configuration
public class RabbitMQConfig {

    public static final String PHOTO_UPLOAD_QUEUE = "photoUploadQueue";

    /**
     * Bean definition for the photo upload queue.
     *
     * @return Queue instance configured as durable.
     */
    @Bean
    public Queue photoUploadQueue() {
        return new Queue(PHOTO_UPLOAD_QUEUE, true); // Durable queue
    }

    /**
     * Configure RabbitMQ Connection Factory.
     *
     * @return ConnectionFactory for RabbitMQ.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("rabbitmq");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    /**
     * RabbitTemplate Bean for sending messages to RabbitMQ.
     *
     * @param connectionFactory The connection factory.
     * @return Configured RabbitTemplate.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     * Listener for receiving messages from RabbitMQ queue.
     *
     * @param rabbitTemplate RabbitTemplate to send responses.
     * @return MessageListenerContainer.
     */
    @Bean
    public MessageListenerContainer messageListenerContainer(RabbitTemplate rabbitTemplate) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(PHOTO_UPLOAD_QUEUE);
        container.setMessageListener(new MessageListenerAdapter(rabbitTemplate));
        return container;
    }
}
