package ski.mashiro.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
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

    @PutMapping("/recover/{commentId}")
    public Result<String> recoverComment(@PathVariable("commentId") Long commentId) {
        return commentService.recoverComment(commentId);
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
        return commentService.pageComment(page, pageSize);
    }

    @GetMapping("/pageUnreviewed")
    public Result<Page<CommentDTO>> pageUnreviewedComment(Long page, Long pageSize) {
        return commentService.pageUnreviewedComment(page, pageSize);
    }

    @GetMapping("/pageDel")
    public Result<Page<CommentDTO>> pageDelComment(Long page, Long pageSize) {
        return commentService.pageDelComment(page, pageSize);
    }

    @GetMapping("/art/{articleId}")
    public Result<Page<CommentViewDTO>> pageCommentByArticleId(@PathVariable("articleId") Long articleId, Long page, Long pageSize) {
        return commentService.pageCommentByArticleId(articleId, page, pageSize);
    }
}
