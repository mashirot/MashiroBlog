package ski.mashiro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class ArticleDTO {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private List<String> category;
    private List<String> tag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
