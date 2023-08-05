package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ski.mashiro.common.Result;
import ski.mashiro.dto.CommentDTO;
import ski.mashiro.dto.CommentUpdateDTO;
import ski.mashiro.entity.Article;
import ski.mashiro.entity.Comment;
import ski.mashiro.mapper.CommentMapper;
import ski.mashiro.service.ArticleService;
import ski.mashiro.service.CommentService;

import java.time.LocalDateTime;
import java.util.Objects;

import static ski.mashiro.constant.StatusConstant.*;

/**
 * @author MashiroT
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final ArticleService articleService;

    public CommentServiceImpl(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> insComment(CommentDTO commentDTO, String remoteHost) {
        if (Objects.isNull(commentDTO) || Objects.isNull(commentDTO.getArticleId())) {
            return Result.failed(COMMENT_INSERT_FAILED, "非法参数");
        }
        if (articleService.count(new LambdaQueryWrapper<Article>().eq(Article::getId, commentDTO.getArticleId())) == 0) {
            return Result.failed(COMMENT_INSERT_FAILED, "评论失败，文章不存在");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setId(null);
        comment.setSenderIp(remoteHost);
        comment.setStatus(1);
        comment.setDeleted(false);
        comment.setCreateTime(LocalDateTime.now());
        save(comment);
        plusArticleCommentCount(commentDTO.getArticleId());
        return Result.success(COMMENT_INSERT_SUCCESS, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> delComment(CommentUpdateDTO commentUpdateDTO) {
        if (Objects.isNull(commentUpdateDTO) || Objects.isNull(commentUpdateDTO.getId())) {
            return Result.failed(COMMENT_DELETE_FAILED, "非法参数");
        }
        Comment comment = getById(commentUpdateDTO.getId());
        if (Objects.isNull(comment) || !comment.getDeleted()) {
            return Result.failed(COMMENT_DELETE_FAILED, "删除失败，评论不存在");
        }
        update(new LambdaUpdateWrapper<Comment>()
                .set(Comment::getDeleted, true)
                .eq(Comment::getId, comment.getId()));
        minusArticleCommentCount(comment.getArticleId());
        return Result.success(COMMENT_DELETE_SUCCESS, null);
    }

    @Override
    public Result<String> updComment(CommentUpdateDTO commentUpdateDTO) {
        if (Objects.isNull(commentUpdateDTO) || Objects.isNull(commentUpdateDTO.getId())) {
            return Result.failed(COMMENT_UPDATE_FAILED, "非法参数");
        }
        return update(new LambdaUpdateWrapper<Comment>()
                .set(Objects.nonNull(commentUpdateDTO.getContent()), Comment::getContent, commentUpdateDTO.getContent())
                .set(Objects.nonNull(commentUpdateDTO.getStatus()), Comment::getStatus, commentUpdateDTO.getStatus())
                .set(Objects.nonNull(commentUpdateDTO.getSecret()), Comment::getSecret, commentUpdateDTO.getSecret())
                .eq(Comment::getId, commentUpdateDTO.getId())
        ) ? Result.success(COMMENT_UPDATE_SUCCESS, null) : Result.failed(COMMENT_UPDATE_FAILED, "更新失败，评论不存在");
    }

    /**
     * 防止并发问题，单端，不考虑分布式并发问题
     */
    private synchronized void plusArticleCommentCount(Long articleId) {
        articleService.update(
                new LambdaUpdateWrapper<Article>()
                        .setSql("comment_count = comment_count + 1")
                        .eq(Article::getId, articleId)
        );
    }

    /**
     * 防止并发问题，单端，不考虑分布式并发问题
     */
    private synchronized void minusArticleCommentCount(Long articleId) {
        articleService.update(
                new LambdaUpdateWrapper<Article>()
                        .setSql("comment_count = comment_count - 1")
                        .eq(Article::getId, articleId)
                        .gt(Article::getCommentCount, 0)
        );
    }
}
