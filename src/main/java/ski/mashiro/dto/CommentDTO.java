package ski.mashiro.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author MashiroT
 */
@Data
public class CommentDTO {
    private Long id;
    private Long articleId;
    private String senderNickname;
    private String senderEmail;
    private String receiverNickname;
    private String receiverEmail;
    private String content;
    private String senderIp;
    private Boolean secret;
    private LocalDateTime createTime;
}
