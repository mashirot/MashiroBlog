package ski.mashiro.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.Result;
import ski.mashiro.dto.ArticlePreviewDTO;
import ski.mashiro.entity.Category;
import ski.mashiro.service.ArticleService;
import ski.mashiro.service.CategoryService;

import java.util.List;

import static ski.mashiro.constant.StatusConsts.*;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ArticleService articleService;

    public CategoryController(CategoryService categoryService, ArticleService articleService) {
        this.categoryService = categoryService;
        this.articleService = articleService;
    }

    @PostMapping
    public Result<String> addCategory(@RequestBody Category category) {
        return categoryService.save(category) ? Result.success(CATEGORY_INSERT_SUCCESS, null) : Result.failed(CATEGORY_INSERT_FAILED, "添加失败，Category重名");
    }

    @DeleteMapping("/{categoryId}")
    public Result<String> delCategory(@PathVariable("categoryId") Long categoryId) {
        return categoryService.delCategory(categoryId);
    }

    @GetMapping
    public Result<List<Category>> listCategory() {
        return Result.success(CATEGORY_SELECT_SUCCESS, categoryService.list());
    }

    @GetMapping("/page")
    public Result<Page<Category>> page(Long page, Long pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        categoryService.page(categoryPage);
        return Result.success(CATEGORY_SELECT_SUCCESS, categoryPage);
    }

    @GetMapping("/{categoryName}")
    public Result<Page<ArticlePreviewDTO>> pageArticle(@PathVariable("categoryName") String categoryName, Long page, Long pageSize) {
        return articleService.pageArticleByCategory(categoryName, page, pageSize);
    }
}
