package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import ski.mashiro.common.Result;
import ski.mashiro.dto.CommentDTO;
import ski.mashiro.dto.CommentViewDTO;
import ski.mashiro.entity.Comment;

/**
 * @author MashiroT
 */
public interface CommentService extends IService<Comment> {
    /**
     * 添加评论
     *
     * @param commentDTO 评论DTO
     * @param remoteHost senderIP
     * @return 结果
     */
    Result<String> insComment(CommentDTO commentDTO, String remoteHost);

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return 结果
     */
    Result<String> delComment(Long commentId);

    /**
     * 恢复评论
     *
     * @param commentId 评论id
     * @return 结果
     */
    Result<String> recoverComment(Long commentId);

    /**
     * 审核评论
     * @param commentId 评论id
     * @return 结果
     */
    Result<String> reviewComment(Long commentId);

    /**
     * 后台用，所有已审核未删除评论分页
     * @param page 当前页
     * @param pageSize 页大小
     * @return 结果
     */
    Result<Page<CommentDTO>> pageComment(Long page, Long pageSize);

    /**
     * 后台用，所有未审核未删除评论分页
     * @param page 当前页
     * @param pageSize 页大小
     * @return 结果
     */
    Result<Page<CommentDTO>> pageUnreviewedComment(Long page, Long pageSize);

    /**
     * 后台用，所有已删除评论分页
     * @param page 当前页
     * @param pageSize 页大小
     * @return 结果
     */
    Result<Page<CommentDTO>> pageDelComment(Long page, Long pageSize);

    /**
     * 展示用，所有已审核非私密评论分页
     * @param articleId 文章id
     * @param page 当前页
     * @param pageSize 页大小
     * @return 结果
     */
    Result<Page<CommentViewDTO>> pageCommentByArticleId(Long articleId, Long page, Long pageSize) throws JsonProcessingException;
}
