package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.Comment;
import ski.mashiro.mapper.CommentMapper;
import ski.mashiro.service.ArticleCommentService;

/**
 * @author MashiroT
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ArticleCommentService {
}
