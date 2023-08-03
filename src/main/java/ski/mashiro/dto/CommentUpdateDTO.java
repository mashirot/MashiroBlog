package ski.mashiro.dto;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class CommentUpdateDTO {
    private Long id;
    private String content;
    private Integer status;
    private Boolean secret;
    private Boolean deleted;
}
