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
    /**
     * 添加文章
     * @param articleDTO 新文章，title、content不为空
     * @return 结果
     */
    Result<String> insArticle(ArticleDTO articleDTO);

    /**
     * 删除文章，逻辑删除，并物理删除两张关联表的行
     * @param articleId 删除文章的id
     * @return 结果
     */
    Result<String> delArticle(Long articleId);

    /**
     * 更新文章
     * @param articleDTO 更新文章的对象，应该包含通过getArticleByArticleId方法获取的全部信息
     * @return 结果
     */
    Result<String> updArticle(ArticleDTO articleDTO);

    /**
     * 通过Id获取未删除文章的全部信息
     * @param articleId 文章Id
     * @return 文章DTO
     */
    Result<ArticleDTO> getArticleByArticleId(Long articleId);

    /**
     * 不加条件获取有效文章并分页，按照创建时间降序
     * @param page 起始页
     * @param pageSize 页大小
     * @return 分页后的PreviewDTO结果
     */
    Result<Page<ArticlePreviewDTO>> pagePreview(Long page, Long pageSize);

    /**
     * 指定标签获取有效文章并分页，按照创建时间降序
     * @param tagName 标签名
     * @param page 起始页
     * @param pageSize 页大小
     * @return 分页后的PreviewDTO结果
     */
    Result<Page<ArticlePreviewDTO>> pageArticleByTag(String tagName, Long page, Long pageSize);

    /**
     * 指定分类获取有效文章并分页，按照创建时间降序
     * @param categoryName 分类名
     * @param page 起始页
     * @param pageSize 页大小
     * @return 分页后的PreviewDTO结果
     */
    Result<Page<ArticlePreviewDTO>> pageArticleByCategory(String categoryName, Long page, Long pageSize);
}
