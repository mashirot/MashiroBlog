package ski.mashiro.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.Result;
import ski.mashiro.dto.ArticlePreviewDTO;
import ski.mashiro.entity.Category;
import ski.mashiro.service.ArticleService;
import ski.mashiro.service.CategoryService;

import java.util.List;

import static ski.mashiro.constant.StatusConstant.*;

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

    @DeleteMapping
    public Result<String> delCategory(@RequestBody Category category) {
        return categoryService.delCategory(category);
    }

    @GetMapping
    public Result<List<Category>> listCategory() {
        return Result.success(CATEGORY_SELECT_SUCCESS, categoryService.list());
    }

    @GetMapping("/{categoryName}")
    public Result<Page<ArticlePreviewDTO>> page(@PathVariable("categoryName") String categoryName, Long page, Long pageSize) {
        return articleService.pageArticleByCategory(categoryName, page, pageSize);
    }
}
