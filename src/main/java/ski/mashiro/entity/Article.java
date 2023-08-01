package ski.mashiro.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author MashiroT
 */
@Data
public class Article {
    private Long id;
    private Long authorId;
    private String content;
    private List<String> category;
    private List<String> tag;
//    0:正常 1:删除
    private Integer delete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
