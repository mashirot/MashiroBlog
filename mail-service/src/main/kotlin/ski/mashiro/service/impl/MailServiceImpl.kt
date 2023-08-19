package ski.mashiro.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service
import ski.mashiro.dto.CommentMailDTO
import ski.mashiro.service.MailService
import java.util.*

/**
 * @author MashiroT
 */
@Service
class MailServiceImpl(
    val objectMapper: ObjectMapper,
    val mailSender: MailSender
) : MailService {
    @Value("\${blog.from}")
    private var from: String? = null
    @Value("\${blog.to}")
    private var to: String? = null
    override fun sendNewCommentAdvice2Owner(msg: String) {
        try {
            val commentMailDTO = objectMapper.readValue(msg, CommentMailDTO::class.java)
            val simpleMailMessage = SimpleMailMessage()
            simpleMailMessage.from = from
            simpleMailMessage.setTo(to)
            simpleMailMessage.subject = "你的帖子「${commentMailDTO.articleTitle}」有新回复"
            simpleMailMessage.text = """
                @${commentMailDTO.senderNickname} 回复了你：
                ${if (commentMailDTO.content!!.length > 50) commentMailDTO.content.substring(0, 50) else commentMailDTO.content}
                """.trimIndent()
            simpleMailMessage.sentDate = Date()
            mailSender.send(simpleMailMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}