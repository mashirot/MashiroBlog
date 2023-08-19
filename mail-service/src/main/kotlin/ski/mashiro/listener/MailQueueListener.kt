package ski.mashiro.listener

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
    @RabbitListener(queues = [RabbitMQConsts.MAIL_QUEUE])
    fun listenMailQueue(msg: String) {
        mailService.sendNewCommentAdvice2Owner(msg)
    }
}