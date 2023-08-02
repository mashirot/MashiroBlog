package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ski.mashiro.common.Result;
import ski.mashiro.dto.ArticleDTO;
import ski.mashiro.dto.ArticlePreviewDTO;
import ski.mashiro.entity.Article;

/**
 * @author MashiroT
 */
public interface ArticleService extends IService<Article> {
    Result<String> addArticle(ArticleDTO articleDTO);

    Result<String> delArticle(ArticleDTO articleDTO);

    Result<String> updArticle(ArticleDTO articleDTO);

    Result<ArticleDTO> getArticleByArticleId(Long articleId);

    Result<Page<ArticlePreviewDTO>> pagePreview(Long page, Long pageSize);

    Result<Page<ArticlePreviewDTO>> pageArticleByTag(String tagName, Long page, Long pageSize);

    Result<Page<ArticlePreviewDTO>> pageArticleByCategory(String categoryName, Long page, Long pageSize);
}
