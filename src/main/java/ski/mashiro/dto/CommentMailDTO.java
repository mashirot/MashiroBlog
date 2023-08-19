package ski.mashiro.dto;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class CommentMailDTO {
    private String articleTitle;
    private String senderNickname;
    private String senderEmail;
    private String receiverNickname;
    private String content;
}
