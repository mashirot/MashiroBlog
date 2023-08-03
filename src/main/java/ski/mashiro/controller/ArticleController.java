package ski.mashiro.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.Result;
import ski.mashiro.dto.ArticleDTO;
import ski.mashiro.dto.ArticlePreviewDTO;
import ski.mashiro.service.ArticleService;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public Result<String> addArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.insArticle(articleDTO);
    }

    @DeleteMapping
    public Result<String> delArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.delArticle(articleDTO);
    }

    @PutMapping
    public Result<String> updArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.updArticle(articleDTO);
    }

    @GetMapping("/{articleId}")
    public Result<ArticleDTO> getArticleByArticleId(@PathVariable("articleId") Long articleId) {
        return articleService.getArticleByArticleId(articleId);
    }

    @GetMapping("/page")
    public Result<Page<ArticlePreviewDTO>> page(Long page, Long pageSize) {
        return articleService.pagePreview(page, pageSize);
    }
}
