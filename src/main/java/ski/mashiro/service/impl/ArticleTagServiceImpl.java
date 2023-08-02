package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.ArticleTag;
import ski.mashiro.mapper.ArticleTagMapper;
import ski.mashiro.service.ArticleTagService;

/**
 * @author MashiroT
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
