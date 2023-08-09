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
public class ArticlePreviewDTO {
    private Long articleId;
    private String title;
    private String previewContent;
    private Integer commentCount;
    private List<String> category;
    private List<String> tag;
    private LocalDateTime createTime;

    public ArticlePreviewDTO(Long articleId, LocalDateTime createTime) {
        this.articleId = articleId;
        this.createTime = createTime;
    }
}
