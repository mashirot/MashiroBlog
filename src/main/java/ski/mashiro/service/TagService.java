package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ski.mashiro.common.Result;
import ski.mashiro.entity.Tag;

/**
 * @author MashiroT
 */
public interface TagService extends IService<Tag> {
    /**
     * 删除Tag并删除ArticleTag表中的关联行
     * @param tagId tagId
     * @return 结果
     */
    Result<String> delTag(Long tagId);
}
