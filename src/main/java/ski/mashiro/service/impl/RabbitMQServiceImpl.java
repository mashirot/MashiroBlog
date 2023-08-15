package ski.mashiro.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.Comment;
import ski.mashiro.service.RabbitMQService;

import java.util.Map;

import static ski.mashiro.constant.RabbitMQConsts.*;

/**
 * @author MashiroT
 */
@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage2MailQueue(String articleTitle, Comment comment) {
        rabbitTemplate.convertAndSend(BLOG_DIRECT_EXCHANGE, MAIL_ROUTING_KEY, Map.of("articleTitle", articleTitle, "comment", comment));
    }
}
