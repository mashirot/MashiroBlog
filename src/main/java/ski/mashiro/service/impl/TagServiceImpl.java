package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ski.mashiro.common.Result;
import ski.mashiro.entity.ArticleTag;
import ski.mashiro.entity.Tag;
import ski.mashiro.mapper.TagMapper;
import ski.mashiro.service.ArticleTagService;
import ski.mashiro.service.TagService;

import java.util.Objects;

import static ski.mashiro.constant.StatusConstant.TAG_DELETE_FAILED;
import static ski.mashiro.constant.StatusConstant.TAG_DELETE_SUCCESS;

/**
 * @author MashiroT
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final ArticleTagService articleTagService;

    public TagServiceImpl(ArticleTagService articleTagService) {
        this.articleTagService = articleTagService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> delTag(Tag tag) {
        if (Objects.isNull(tag) || !StringUtils.hasText(tag.getName())) {
            return Result.failed(TAG_DELETE_FAILED, "非法参数");
        }
        tag = getOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tag.getName()));
        if (Objects.isNull(tag)) {
            return Result.failed(TAG_DELETE_FAILED, "删除失败，Tag不存在");
        }
        articleTagService.remove(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tag.getId()));
        removeById(tag);
        return Result.success(TAG_DELETE_SUCCESS, null);
    }
}
