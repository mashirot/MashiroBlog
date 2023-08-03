package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ski.mashiro.entity.Comment;

/**
 * 用于解决ArticleService和CategoryService的循环依赖
 * @author MashiroT
 */
public interface ArticleCommentService extends IService<Comment> {
}
