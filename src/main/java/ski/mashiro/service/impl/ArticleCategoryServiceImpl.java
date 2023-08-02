package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.ArticleCategory;
import ski.mashiro.mapper.ArticleCategoryMapper;
import ski.mashiro.service.ArticleCategoryService;

/**
 * @author MashiroT 
 */
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {
}
