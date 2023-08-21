package ski.mashiro.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @DeleteMapping("/{articleId}")
    public Result<String> delArticle(@PathVariable("articleId") Long articleId) {
        return articleService.delArticle(articleId);
    }

    @PutMapping("/recover/{articleId}")
    public Result<String> recoverArticle(@PathVariable("articleId") Long articleId) {
        return articleService.recoverArticle(articleId);
    }

    @PutMapping
    public Result<String> updArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.updArticle(articleDTO);
    }

    @GetMapping("/{articleId}")
    public Result<ArticleDTO> getArticleByArticleId(@PathVariable("articleId") Long articleId) throws JsonProcessingException {
        return articleService.getArticleByArticleId(articleId);
    }

    @GetMapping("/page")
    public Result<Page<ArticlePreviewDTO>> page(Long page, Long pageSize) {
        return articleService.pagePreview(page, pageSize);
    }

    @GetMapping("/pageDel")
    public Result<Page<ArticlePreviewDTO>> pageDel(Long page, Long pageSize) {
        return articleService.pageDelPreview(page, pageSize);
    }
}
