package ski.mashiro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.Result;
import ski.mashiro.dto.CommentDTO;
import ski.mashiro.dto.CommentUpdateDTO;
import ski.mashiro.dto.CommentViewDTO;
import ski.mashiro.entity.Comment;
import ski.mashiro.service.CommentService;

import java.util.Objects;

import static ski.mashiro.constant.StatusConstant.COMMENT_SELECT_FAILED;
import static ski.mashiro.constant.StatusConstant.COMMENT_SELECT_SUCCESS;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Result<String> addComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        return commentService.insComment(commentDTO, request.getRemoteAddr());
    }

    @DeleteMapping("/{commentId}")
    public Result<String> delComment(@PathVariable("commentId") Long commentId) {
        return commentService.delComment(commentId);
    }

    @PutMapping("/reply/{commentId}")
    public Result<String> replyComment(@PathVariable("commentId") Long commentId) {
        return commentService.replyComment(commentId);
    }

    @PutMapping
    public Result<String> updComment(@RequestBody CommentUpdateDTO commentUpdateDTO) {
        return commentService.updComment(commentUpdateDTO);
    }

    @PutMapping("/review/{commentId}")
    public Result<String> reviewComment(@PathVariable("commentId") Long commentId) {
        return commentService.reviewComment(commentId);
    }

    @GetMapping("/{commentId}")
    public Result<Comment> getCommentByCommentId(@PathVariable("commentId") Long commentId) {
        Comment comment = commentService.getById(commentId);
        return Objects.nonNull(comment) && !comment.getDeleted() ? Result.success(COMMENT_SELECT_SUCCESS, comment) : Result.failed(COMMENT_SELECT_FAILED, "评论不存在");
    }

    @GetMapping("/page")
    public Result<Page<CommentDTO>> pageComment(Long page, Long pageSize) {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        commentService.page(commentPage,
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getDeleted, false)
                        .eq(Comment::getStatus, 0)
                        .orderByDesc(Comment::getCreateTime)
        );
        Page<CommentDTO> commentDTOPage = getCommentDTOPage(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentDTOPage);
    }

    @GetMapping("/pageUnreviewed")
    public Result<Page<CommentDTO>> pageUnreviewedComment(Long page, Long pageSize) {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        commentService.page(commentPage,
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

    @GetMapping("/pageDel")
    public Result<Page<CommentDTO>> pageDelComment(Long page, Long pageSize) {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        commentService.page(commentPage,
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getDeleted, true)
                        .orderByDesc(Comment::getCreateTime)
        );
        Page<CommentDTO> commentDTOPage = getCommentDTOPage(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentDTOPage);
    }

    @GetMapping("/art/{articleId}")
    public Result<Page<CommentViewDTO>> pageCommentByArticleId(@PathVariable("articleId") Long articleId, Long page, Long pageSize) {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        commentService.page(
                commentPage,
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getArticleId, articleId)
                        // 未删除
                        .eq(Comment::getDeleted, false)
                        // 已审核
                        .eq(Comment::getStatus, 0)
                        // 非私密
                        .eq(Comment::getSecret, false)
                        .orderByDesc(Comment::getCreateTime)
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

    private static Page<CommentDTO> getCommentDTOPage(Page<Comment> commentPage) {
        Page<CommentDTO> commentDTOPage = new Page<>();
        BeanUtils.copyProperties(commentPage, commentDTOPage, "records");
        commentDTOPage.setRecords(commentPage.getRecords().stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            return commentDTO;
        }).toList());
        return commentDTOPage;
    }
}
