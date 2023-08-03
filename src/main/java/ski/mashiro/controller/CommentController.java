package ski.mashiro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.Result;
import ski.mashiro.dto.CommentDTO;
import ski.mashiro.dto.CommentUpdateDTO;
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
    public Result<String> addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.insComment(commentDTO);
    }

    @DeleteMapping
    public Result<String> delComment(@RequestBody CommentUpdateDTO commentUpdateDTO) {
        return commentService.delComment(commentUpdateDTO);
    }

    @PutMapping
    public Result<String> updComment(@RequestBody CommentUpdateDTO commentUpdateDTO) {
        return commentService.updComment(commentUpdateDTO);
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
                        .orderByDesc(Comment::getCreateTime)
        );
        Page<CommentDTO> commentDTOPage = getCommentDTOPage(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentDTOPage);
    }

    @GetMapping("/art/{articleId}")
    public Result<Page<CommentDTO>> pageCommentByArticleId(@PathVariable("articleId") Long articleId, Long page, Long pageSize) {
        if (Objects.isNull(page) || Objects.isNull(pageSize)) {
            return Result.failed(COMMENT_SELECT_FAILED, "非法参数");
        }
        Page<Comment> commentPage = new Page<>(page, pageSize);
        commentService.page(
                commentPage,
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getArticleId, articleId)
                        .eq(Comment::getDeleted, false)
                        .orderByDesc(Comment::getCreateTime)
        );
        Page<CommentDTO> commentDTOPage = getCommentDTOPage(commentPage);
        return Result.success(COMMENT_SELECT_SUCCESS, commentDTOPage);
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
