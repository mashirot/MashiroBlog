package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ski.mashiro.common.JwtInfo;
import ski.mashiro.common.Result;
import ski.mashiro.dto.ArticleDTO;
import ski.mashiro.dto.ArticlePreviewDTO;
import ski.mashiro.entity.*;
import ski.mashiro.mapper.ArticleMapper;
import ski.mashiro.service.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ski.mashiro.constant.StatusConstant.*;

/**
 * @author MashiroT
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final TagService tagService;
    private final CategoryService categoryService;
    private final ArticleTagService articleTagService;
    private final ArticleCategoryService articleCategoryService;
    private final CommentService commentService;

    public ArticleServiceImpl(TagService tagService, CategoryService categoryService, ArticleTagService articleTagService, ArticleCategoryService articleCategoryService, CommentService commentService) {
        this.tagService = tagService;
        this.categoryService = categoryService;
        this.articleTagService = articleTagService;
        this.articleCategoryService = articleCategoryService;
        this.commentService = commentService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> addArticle(ArticleDTO articleDTO) {
        if (Objects.isNull(articleDTO)) {
            return Result.failed(ARTICLE_INSERT_FAILED, "非法参数");
        }
        JwtInfo jwtInfo = (JwtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Article article = new Article();
        article.setAuthorId(jwtInfo.id());
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setIsDelete(0);
        LocalDateTime now = LocalDateTime.now();
        article.setCreateTime(now);
        article.setUpdateTime(now);
        save(article);
        Long articleId = article.getId();

        if (Objects.nonNull(articleDTO.getTag())) {
            addRelationTags(articleDTO, articleId);
        }

        if (Objects.nonNull(articleDTO.getCategory())) {
            addRelationCategories(articleDTO, articleId);
        }

        return Result.success(ARTICLE_INSERT_SUCCESS, null);
    }

    @Transactional
    @Override
    public Result<String> delArticle(ArticleDTO articleDTO) {
        if (Objects.isNull(articleDTO) || Objects.isNull(articleDTO.getId())) {
            return Result.failed(ARTICLE_DELETE_FAILED, "非法参数");
        }
        Article article = getOne(new LambdaQueryWrapper<Article>().eq(Article::getId, articleDTO.getId()).eq(Article::getIsDelete, 0));
        if (Objects.isNull(article)) {
            return Result.failed(ARTICLE_DELETE_FAILED, "删除失败，Article不存在");
        }
        article.setIsDelete(1);
        article.setUpdateTime(LocalDateTime.now());
        updateById(article);
        articleTagService.remove(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, article.getId()));
        articleCategoryService.remove(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getArticleId, article.getId()));
        return Result.success(ARTICLE_DELETE_SUCCESS, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> updArticle(ArticleDTO articleDTO) {
        if (Objects.isNull(articleDTO) || Objects.isNull(articleDTO.getId()) || Objects.isNull(articleDTO.getTitle()) || Objects.isNull(articleDTO.getContent())) {
            return Result.failed(ARTICLE_UPDATE_FAILED, "非法参数");
        }
        Article article = getOne(new LambdaQueryWrapper<Article>().eq(Article::getId, articleDTO.getId()).eq(Article::getIsDelete, 0));
        if (Objects.isNull(article)) {
            return Result.failed(ARTICLE_UPDATE_FAILED, "删除失败，Article不存在");
        }
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setUpdateTime(LocalDateTime.now());
        updateById(article);

        Long articleId = article.getId();

        if (Objects.nonNull(articleDTO.getTag())) {
//            先删再加
            articleTagService.remove(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
            addRelationTags(articleDTO, articleId);
        }

        if (Objects.nonNull(articleDTO.getCategory())) {
            articleCategoryService.remove(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getArticleId, articleId));
            addRelationCategories(articleDTO, articleId);
        }

        return Result.success(ARTICLE_UPDATE_SUCCESS, null);
    }

    @Override
    public Result<ArticleDTO> getArticleByArticleId(Long articleId) {
        if (Objects.isNull(articleId)) {
            return Result.failed(ARTICLE_SELECT_FAILED, "非法参数");
        }
        Article article = getOne(new LambdaQueryWrapper<Article>().eq(Article::getId, articleId).eq(Article::getIsDelete, 0));
        if (Objects.isNull(article)) {
            return Result.failed(ARTICLE_SELECT_FAILED, "Article不存在");
        }
        List<Tag> tags = getArticleTags(articleId);
        article.setTag(tags.stream().map(Tag::getName).toList());
        List<Category> categories = getArticleCategories(articleId);
        article.setCategory(categories.stream().map(Category::getName).toList());
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtils.copyProperties(article, articleDTO);
        return Result.success(ARTICLE_SELECT_SUCCESS, articleDTO);
    }

    @Override
    public Result<Page<ArticlePreviewDTO>> pagePreview(Long page, Long pageSize) {
        Page<Article> articlePage = new Page<>(page, pageSize);
        page(articlePage, new LambdaQueryWrapper<Article>().eq(Article::getIsDelete, 0).orderByDesc(Article::getCreateTime));
        Page<ArticlePreviewDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(articlePage, dtoPage, "records");
        dtoPage.setRecords(articlePage.getRecords().stream().map(this::getPreviewDTOByArticle).toList());
        return Result.success(TAG_SELECT_SUCCESS, dtoPage);
    }

    @Override
    public Result<Page<ArticlePreviewDTO>> pageArticleByTag(String tagName, Long page, Long pageSize) {
        if (!StringUtils.hasText(tagName)) {
            return Result.failed(TAG_SELECT_SUCCESS, "非法参数");
        }
        Tag tag = tagService.getOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tagName));
        if (Objects.isNull(tag)) {
            return Result.failed(TAG_SELECT_FAILED, "Tag不存在");
        }
        List<ArticleTag> relationArticles = articleTagService.list(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tag.getId()));
        Page<ArticlePreviewDTO> dtoPage = getPreviewPageIn(page, pageSize, relationArticles.stream().map(ArticleTag::getArticleId).toList());
        return Result.success(TAG_SELECT_SUCCESS, dtoPage);
    }

    @Override
    public Result<Page<ArticlePreviewDTO>> pageArticleByCategory(String categoryName, Long page, Long pageSize) {
        if (!StringUtils.hasText(categoryName)) {
            return Result.failed(CATEGORY_SELECT_FAILED, "非法参数");
        }
        Category category = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getName, categoryName));
        if (Objects.isNull(category)) {
            return Result.failed(CATEGORY_SELECT_FAILED, "Category不存在");
        }
        List<ArticleCategory> relationCategories = articleCategoryService.list(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getCategoryId, category.getId()));
        Page<ArticlePreviewDTO> dtoPage = getPreviewPageIn(page, pageSize, relationCategories.stream().map(ArticleCategory::getArticleId).toList());
        return Result.success(CATEGORY_SELECT_SUCCESS, dtoPage);
    }

    private void addRelationTags(ArticleDTO articleDTO, Long articleId) {
//        获取Tag
        List<String> tags = articleDTO.getTag();
        if (Objects.nonNull(tags)) {
//        获取存在的Tag
            List<Tag> existedTag = tagService.list(new LambdaQueryWrapper<Tag>().in(Tag::getName, tags));
            Set<String> existedTagSet = existedTag.stream().map(Tag::getName).collect(Collectors.toSet());
//        判断是否有新Tag，并添加到已存在的TagList
            tags.forEach(tag -> {
                if (!existedTagSet.contains(tag)) {
                    Tag t = new Tag(tag);
                    tagService.save(t);
                    existedTag.add(t);
                }
            });
//        中间表插入
            List<ArticleTag> articleTags = existedTag.stream().map(tag -> new ArticleTag(articleId, tag.getId())).toList();
            articleTagService.saveBatch(articleTags);
        }
    }

    private void addRelationCategories(ArticleDTO articleDTO, Long articleId) {
        List<String> categories = articleDTO.getCategory();
        if (Objects.nonNull(categories)) {
            List<Category> existedCategory = categoryService.list(new LambdaQueryWrapper<Category>().in(Category::getName, categories));
            Set<String> existedCategorySet = existedCategory.stream().map(Category::getName).collect(Collectors.toSet());
            categories.forEach(category -> {
                if (!existedCategorySet.contains(category)) {
                    Category c = new Category(category);
                    categoryService.save(c);
                    existedCategory.add(c);
                }
            });
            List<ArticleCategory> articleCategories = existedCategory.stream().map(category -> new ArticleCategory(articleId, category.getId())).toList();
            articleCategoryService.saveBatch(articleCategories);
        }
    }

    private ArticlePreviewDTO getPreviewDTOByArticle(Article article) {
        ArticlePreviewDTO previewDTO = new ArticlePreviewDTO(article.getId(), article.getCreateTime());
        previewDTO.setTitle(article.getTitle());
        previewDTO.setPreviewContent(article.getContent());
        if (article.getContent().length() > 50) {
            previewDTO.setPreviewContent(article.getContent().substring(0, 50));
        }
        previewDTO.setCommentCount(commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, article.getId())));
        List<Tag> tags = getArticleTags(article.getId());
        previewDTO.setTag(tags.stream().map(Tag::getName).toList());
        return previewDTO;
    }

    private Page<ArticlePreviewDTO> getPreviewPageIn(Long page, Long pageSize, List<Long> articleIds) {
        Page<Article> articlePage = new Page<>(page, pageSize);
        page(articlePage, new LambdaQueryWrapper<Article>().eq(Article::getIsDelete, 0).in(Article::getId, articleIds).orderByDesc(Article::getCreateTime));
        Page<ArticlePreviewDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(articlePage, dtoPage, "records");
        dtoPage.setRecords(articlePage.getRecords().stream().map(this::getPreviewDTOByArticle).toList());
        return dtoPage;
    }

    private List<Category> getArticleCategories(Long articleId) {
        List<ArticleCategory> relationCategories = articleCategoryService.list(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getArticleId, articleId));
        return categoryService.listByIds(relationCategories.stream().map(ArticleCategory::getCategoryId).toList());
    }

    private List<Tag> getArticleTags(Long articleId) {
        List<ArticleTag> relationTags = articleTagService.list(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
        return tagService.listByIds(relationTags.stream().map(ArticleTag::getTagId).toList());
    }
}
