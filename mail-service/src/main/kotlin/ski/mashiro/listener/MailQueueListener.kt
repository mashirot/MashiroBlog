package ski.mashiro.listener

import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ski.mashiro.constant.RabbitMQConsts
import ski.mashiro.service.MailService

/**
 * @author MashiroT
 */
@Component
class MailQueueListener(
    val mailService: MailService
) {
    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(RabbitMQConsts.MAIL_QUEUE),
            exchange = Exchange(RabbitMQConsts.BLOG_DIRECT_EXCHANGE)
        )]
    )
    fun listenMailQueue(msg: String) {
        mailService.sendNewCommentAdvice2Owner(msg)
    }
}