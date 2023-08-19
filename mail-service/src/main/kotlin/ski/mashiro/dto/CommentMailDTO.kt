package ski.mashiro.dto

/**
 * @author MashiroT
 */
class CommentMailDTO {
    val articleTitle: String? = null
    val senderNickname: String? = null
    val senderEmail: String? = null
    val receiverNickname: String? = null
    val content: String? = null

    override fun toString(): String {
        return "CommentMailDTO(articleTitle=$articleTitle, senderNickname=$senderNickname, senderEmail=$senderEmail, receiverNickname=$receiverNickname, content=$content)"
    }
}
