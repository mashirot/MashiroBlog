package ski.mashiro.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ski.mashiro.dto.CommentMailDTO;
import ski.mashiro.entity.Comment;
import ski.mashiro.service.RabbitMQService;

import static ski.mashiro.constant.RabbitMQConsts.BLOG_DIRECT_EXCHANGE;
import static ski.mashiro.constant.RabbitMQConsts.MAIL_ROUTING_KEY;

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
        CommentMailDTO commentMailDTO = new CommentMailDTO();
        BeanUtils.copyProperties(comment, commentMailDTO, "articleTitle");
        commentMailDTO.setArticleTitle(articleTitle);
        rabbitTemplate.convertAndSend(BLOG_DIRECT_EXCHANGE, MAIL_ROUTING_KEY, commentMailDTO);
    }

}
