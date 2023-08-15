package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ski.mashiro.common.Result;
import ski.mashiro.entity.ArticleCategory;
import ski.mashiro.entity.Category;
import ski.mashiro.mapper.CategoryMapper;
import ski.mashiro.service.ArticleCategoryService;
import ski.mashiro.service.CategoryService;

import java.util.Objects;

import static ski.mashiro.constant.StatusConstant.CATEGORY_DELETE_FAILED;
import static ski.mashiro.constant.StatusConstant.CATEGORY_DELETE_SUCCESS;

/**
 * @author MashiroT
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleCategoryService articleCategoryService;

    public CategoryServiceImpl(ArticleCategoryService articleCategoryService) {
        this.articleCategoryService = articleCategoryService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> delCategory(Long categoryId) {
        if (Objects.isNull(categoryId)) {
            return Result.failed(CATEGORY_DELETE_FAILED, "非法参数");
        }
        var category = getById(categoryId);
        if (Objects.isNull(category)) {
            return Result.failed(CATEGORY_DELETE_FAILED, "删除失败，Category不存在");
        }
        articleCategoryService.remove(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getCategoryId, category.getId()));
        removeById(category);
        return Result.success(CATEGORY_DELETE_SUCCESS, null);
    }
}
