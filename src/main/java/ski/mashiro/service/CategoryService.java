package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ski.mashiro.common.Result;
import ski.mashiro.entity.Category;

/**
 * @author MashiroT
 */
public interface CategoryService extends IService<Category> {
    /**
     * 删除Category并删除ArticleCategory表中的关联行
     * @param categoryId categoryId
     * @return 结果
     */
    Result<String> delCategory(Long categoryId);
}
