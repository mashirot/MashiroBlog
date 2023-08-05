package ski.mashiro.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author MashiroT
 */
@Data
public class CommentViewDTO {
    private Long id;
    private Long articleId;
    private String senderNickname;
    private String senderEmailMD5;
    private String content;
    private LocalDateTime createTime;
}
