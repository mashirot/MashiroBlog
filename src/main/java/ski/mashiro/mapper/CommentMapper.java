package ski.mashiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ski.mashiro.entity.Comment;

/**
 * @author MashiroT
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
