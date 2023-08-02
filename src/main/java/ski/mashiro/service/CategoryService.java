package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ski.mashiro.common.Result;
import ski.mashiro.entity.Category;

/**
 * @author MashiroT
 */
public interface CategoryService extends IService<Category> {
    Result<String> delCategory(Category category);
}
