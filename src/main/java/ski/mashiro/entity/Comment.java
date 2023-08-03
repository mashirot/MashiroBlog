package ski.mashiro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author MashiroT
 */
@Data
public class Comment {
    private Long id;
    private Long articleId;
    private String senderNickname;
    private String senderEmail;
    private String receiverNickname;
    private String receiverEmail;
    private String content;
    private String senderIp;
    /**
     * 0:正常 1:待审核
     */
    private Integer status;
    /**
     * 0:普通 1:私密
     */
    @TableField("is_secret")
    private Boolean secret;
    /**
     * 0:正常 1:删除
     */
    @TableField("is_delete")
    private Boolean delete;
    private LocalDateTime createTime;
}
