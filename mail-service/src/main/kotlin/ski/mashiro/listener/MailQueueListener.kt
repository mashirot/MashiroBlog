package ski.mashiro.listener

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ski.mashiro.constant.RabbitMQConsts

/**
 * @author MashiroT
 */
@Component
class MailQueueListener {

    @RabbitListener(queues = [RabbitMQConsts.MAIL_QUEUE])
    fun listenMailQueue(msg: String) {
        println(msg)
    }
}