package ski.mashiro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class ArticleCategory {
    private Long id;
    private Long articleId;
    private Long categoryId;

    public ArticleCategory(Long articleId, Long categoryId) {
        this.articleId = articleId;
        this.categoryId = categoryId;
    }
}
