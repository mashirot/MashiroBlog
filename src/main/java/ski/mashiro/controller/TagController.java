package ski.mashiro.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.Result;
import ski.mashiro.dto.ArticlePreviewDTO;
import ski.mashiro.entity.Tag;
import ski.mashiro.service.ArticleService;
import ski.mashiro.service.TagService;

import static ski.mashiro.constant.StatusConstant.TAG_INSERT_FAILED;
import static ski.mashiro.constant.StatusConstant.TAG_INSERT_SUCCESS;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;
    private final ArticleService articleService;

    public TagController(TagService tagService, ArticleService articleService) {
        this.tagService = tagService;
        this.articleService = articleService;
    }

    @PostMapping
    public Result<String> addTag(@RequestBody Tag tag) {
        return tagService.save(tag) ? Result.success(TAG_INSERT_SUCCESS, null) : Result.failed(TAG_INSERT_FAILED, "添加失败，Tag重名");
    }

    @DeleteMapping
    public Result<String> delTag(@RequestBody Tag tag) {
        return tagService.delTag(tag);
    }

    @GetMapping("/{tagName}")
    public Result<Page<ArticlePreviewDTO>> page(@PathVariable("tagName") String tagName, Long page, Long pageSize) {
        return articleService.pageArticleByTag(tagName, page, pageSize);
    }
}
