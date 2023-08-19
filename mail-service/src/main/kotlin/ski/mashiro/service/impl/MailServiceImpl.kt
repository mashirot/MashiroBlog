package ski.mashiro.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import freemarker.template.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import ski.mashiro.dto.CommentMailDTO
import ski.mashiro.service.MailService
import java.util.*

/**
 * @author MashiroT
 */
@Service
class MailServiceImpl(
    val objectMapper: ObjectMapper,
    val mailSender: JavaMailSender,
    val freeMakerConfiguration: Configuration
) : MailService {
    @Value("\${blog.from}")
    private var from: String? = null

    @Value("\${blog.to}")
    private var to: String? = null

    override fun sendNewCommentAdvice2Owner(msg: String) {
        try {
            val commentMailDTO = objectMapper.readValue(msg, CommentMailDTO::class.java)

            val mimeMessage = mailSender.createMimeMessage()
            val mimeMessageHelper = MimeMessageHelper(mimeMessage, "utf-8")
            mimeMessageHelper.setFrom(from!!)
            mimeMessageHelper.setTo(to!!)
            val title = "你的帖子「${commentMailDTO.articleTitle}」有新回复"
            mimeMessageHelper.setSubject(title)

            val map = mapOf(
                "title" to title,
                "senderNickname" to commentMailDTO.senderNickname!!,
                "content" to if (commentMailDTO.content!!.length > 50) commentMailDTO.content.substring(
                    0,
                    50
                ) else commentMailDTO.content
            )
            val template = freeMakerConfiguration.getTemplate("CommentAdvice.ftlh")
            val text = FreeMarkerTemplateUtils.processTemplateIntoString(template, map)

            mimeMessageHelper.setText(text, true)
            mimeMessageHelper.setSentDate(Date())

            mailSender.send(mimeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}