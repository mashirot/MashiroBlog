package ski.mashiro.service;

import ski.mashiro.entity.Comment;

/**
 * @author MashiroT
 */
public interface RabbitMQService {
    void sendMessage2MailQueue(String articleTitle, Comment comment);
}
