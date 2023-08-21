package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import ski.mashiro.common.Result;
import ski.mashiro.dto.CommentDTO;
import ski.mashiro.dto.CommentViewDTO;
import ski.mashiro.entity.Article;
import ski.mashiro.entity.Comment;
import ski.mashiro.mapper.CommentMapper;
import ski.mashiro.service.ArticleService;
import ski.mashiro.service.CommentService;
import ski.mashiro.service.RabbitMQService;
import ski.mashiro.util.RedisUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static ski.mashiro.constant.RedisConsts.*;
import static ski.mashiro.constant.StatusConsts.*;

/**
 * @author MashiroT
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final ArticleService articleService;
    private final RabbitMQService rabbitMQService;
    private final RedisUtils redisUtils;

    public CommentServiceImpl(ArticleService articleService, RabbitMQService rabbitMQService, RedisUtils redisUtils) {
        this.articleService = articleService;
        this.rabbitMQService = rabbitMQService;
        this.redisUtils = redisUtils;
    }

    @Override
    public Result<String> insComment(CommentDTO commentDTO, String remoteHost) {
        if (Objects.isNull(commentDTO) || Objects.isNull(commentDTO.getArticleId())) {
            return Result.failed(COMMENT_INSERT_FAILED, "非法参数");
        }
        Article article = articleService.getById(commentDTO.getArticleId());
        if (Objects.isNull(article)) {
            return Result.failed(COMMENT_INSERT_FAILED, "评论失败，文章不存在");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setId(null);
        comment.setSenderIp(remoteHost);
        comment.setReplyCommentId(commentDTO.getReplyCommentId());
        comment.setReceiverNickname(commentDTO.getReceiverNickname());
        comment.setStatus(1);
        comment.setDeleted(false);
        comment.setCreateTime(LocalDateTime.now());
        save(comment);
        rabbitMQService.sendMessage2MailQueue(article.getTitle(), comment);
        return Result.success(COMMENT_INSERT_SUCCESS, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> delComment(Long commentId) {
        if (Objects.isNull(commentId)) {
            return Result.failed(COMMENT_DELETE_FAILED, "非法参数");
        }
        Comment comment = getById(commentId);
        if (Objects.isNull(comment) || comment.getDeleted()) {
            return Result.failed(COMMENT_DELETE_FAILED, "删除失败，评论不存在或已被删除");
        }
        update(new LambdaUpdateWrapper<Comment>()
                .set(Comment::getDeleted, true)
                .eq(Comment::getId, comment.getId()));
        minusArticleCommentCount(comment.getArticleId());
        redisUtils.delete(COMMENT_ARTICLE_KEY + comment.getArticleId());
        return Result.success(COMMENT_DELETE_SUCCESS, null);
    }

    @Override
    public Result<String> recoverComment(Long commentId) {
        if (Objects.isNull(commentId)) {
            return Result.failed(COMMENT_UPDATE_FAILED, "非法参数");
        }
        Comment comment = getById(commentId);
        if (Objects.isNull(comment) || !comment.getDeleted()) {
            return Result.failed(COMMENT_UPDATE_FAILED, "恢复失败，评论不存在或未被删除");
        }
        update(new LambdaUpdateWrapper<Comment>()
                .set(Comment::getDeleted, false)
                .eq(Comment::getId, comment.getId()));
        plusArticleCommentCount(comment.getArticleId());
        redisUtils.delete(COMMENT_ARTICLE_KEY + comment.getArticleId());
        return Result.success(COMMENT_UPDATE_SUCCESS, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> reviewComment(Long commentId) {
        if (Objects.isNull(commentId)) {
            return Result.failed(COMMENT_UPDATE_FAILED, "非法参数");
        }
        Comment comment = getById(commentId);
        if (Objects.isNull(comment) || comment.getDeleted()) {
            return Result.failed(COMMENT_UPDATE_FAILED, "审核失败，评论不存在或已删除");
        }
        update(new LambdaUpdateWrapper<Comment>().set(Comment::getStatus, 0).eq(Comment::getId, comment.getId()));
        plusArticleCommentCount(comment.getArticleId());
        redisUtils.delete(COMMENT_ARTICLE_KEY + comment.getArticleId());
        return Result.success(COMMENT_UPDATE_SUCCESS, null);
    }

    @Override
    public Result<Page<CommentDTO>> pageComment(Long page, Long pageSize) {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        page(commentPage,
                new LambdaQueryWrapper<Comment>()
                        // 未删除
                        .eq(Comment::getDeleted, false)
                        // 已审核
                        .eq(Comment::getStatus, 0)
                        .orderByDesc(Comment::getCreateTime)
        );
        Page<CommentDTO> commentDTOPage = getCommentDTOPage(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentDTOPage);
    }

    @Override
    public Result<Page<CommentDTO>> pageUnreviewedComment(Long page, Long pageSize) {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        page(commentPage,
                new LambdaQueryWrapper<Comment>()
                        // 未删除
                        .eq(Comment::getDeleted, false)
                        // 未审核
                        .eq(Comment::getStatus, 1)
                        .orderByDesc(Comment::getCreateTime)
        );
        Page<CommentDTO> commentDTOPage = getCommentDTOPage(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentDTOPage);
    }

    @Override
    public Result<Page<CommentDTO>> pageDelComment(Long page, Long pageSize) {

        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        page(commentPage,
                new LambdaQueryWrapper<Comment>()
                        // 已删除
                        .eq(Comment::getDeleted, true)
                        .orderByDesc(Comment::getCreateTime)
        );
        Page<CommentDTO> commentDTOPage = getCommentDTOPage(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentDTOPage);
    }

    @Override
    public Result<Page<CommentViewDTO>> pageCommentByArticleId(Long articleId, Long page, Long pageSize) throws JsonProcessingException {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage
                = redisUtils.getOrSetCache(
                COMMENT_ARTICLE_KEY + articleId + COMMENT_ARTICLE_PAGE_KEY + page,
                articleId,
                Page.class,
                Comment.class,
                COMMENT_ARTICLE_TTL,
                TimeUnit.SECONDS,
                (id) ->
                        page(
                                new Page<>(page, pageSize),
                                new LambdaQueryWrapper<Comment>()
                                        .eq(Comment::getArticleId, id)
                                        // 未删除
                                        .eq(Comment::getDeleted, false)
                                        // 已审核
                                        .eq(Comment::getStatus, 0)
                                        // 非私密
                                        .eq(Comment::getSecret, false)
                                        .orderByDesc(Comment::getCreateTime)
                        )
        );
        Page<CommentViewDTO> commentViewDTOPage = getCommentViewDTO(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentViewDTOPage);
    }

    private Page<CommentViewDTO> getCommentViewDTO(Page<Comment> commentPage) {
        Page<CommentViewDTO> commentViewDTOPage = new Page<>();
        BeanUtils.copyProperties(commentPage, commentViewDTOPage, "records");
        commentViewDTOPage.setRecords(commentPage.getRecords().stream().map(comment -> {
            CommentViewDTO commentViewDTO = new CommentViewDTO();
            BeanUtils.copyProperties(comment, commentViewDTO);
            commentViewDTO.setSenderEmailMD5(DigestUtils.md5DigestAsHex(comment.getSenderEmail().toLowerCase().getBytes()));
            return commentViewDTO;
        }).toList());
        return commentViewDTOPage;
    }

    private Page<CommentDTO> getCommentDTOPage(Page<Comment> commentPage) {
        Page<CommentDTO> commentDTOPage = new Page<>();
        BeanUtils.copyProperties(commentPage, commentDTOPage, "records");
        commentDTOPage.setRecords(commentPage.getRecords().stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            return commentDTO;
        }).toList());
        return commentDTOPage;
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
