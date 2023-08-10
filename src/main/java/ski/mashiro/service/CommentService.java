package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ski.mashiro.common.Result;
import ski.mashiro.dto.CommentDTO;
import ski.mashiro.dto.CommentUpdateDTO;
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
    Result<String> replyComment(Long commentId);

    /**
     * 更新评论
     * @param commentUpdateDTO 更新DTO，id不为空，不包含deleted
     * @return 结果
     */
    Result<String> updComment(CommentUpdateDTO commentUpdateDTO);

    /**
     * 审核评论
     * @param commentId 评论id
     * @return 结果
     */
    Result<String> reviewComment(Long commentId);
}
