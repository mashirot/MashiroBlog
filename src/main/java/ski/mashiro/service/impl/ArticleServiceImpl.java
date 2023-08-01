package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.Article;
import ski.mashiro.mapper.ArticleMapper;
import ski.mashiro.service.ArticleService;

/**
 * @author MashiroT
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
