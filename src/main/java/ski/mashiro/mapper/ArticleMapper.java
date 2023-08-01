package ski.mashiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ski.mashiro.entity.Article;

/**
 * @author MashiroT
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
