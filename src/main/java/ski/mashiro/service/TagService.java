package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ski.mashiro.common.Result;
import ski.mashiro.entity.Tag;

/**
 * @author MashiroT
 */
public interface TagService extends IService<Tag> {
    Result<String> delTag(Tag tagName);
}
