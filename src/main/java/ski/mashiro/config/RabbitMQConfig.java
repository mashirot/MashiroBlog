package ski.mashiro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ski.mashiro.constant.RabbitMQConsts.*;

/**
 * @author MashiroT
 */
@Configuration
public class RabbitMQConfig {

    private final ObjectMapper objectMapper;

    public RabbitMQConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(BLOG_DIRECT_EXCHANGE);
    }

    @Bean
    public Queue mailQueue() {
        return new Queue(MAIL_QUEUE);
    }

    @Bean
    public Queue pushdeerQueue() {
        return new Queue(PUSHDEER_QUEUE);
    }

    @Bean
    public Binding bindingMailQueue2Exchange() {
        return BindingBuilder.bind(mailQueue()).to(directExchange()).with(MAIL_ROUTING_KEY);
    }

    @Bean
    public Binding bindingPushdeerQueue2Exchange() {
        return BindingBuilder.bind(pushdeerQueue()).to(directExchange()).with(PUSHDEER_ROUTING_KEY);
    }
}
