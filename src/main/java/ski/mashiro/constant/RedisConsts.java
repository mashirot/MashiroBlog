package ski.mashiro.constant;

/**
 * @author MashiroT
 */
public class RedisConsts {
    public static final String USER_JWT_KEY = "user:jwt:";
    public static final String ARTICLE_CACHE_KEY = "article:id:";
    public static final long ARTICLE_CACHE_TTL = 3600;
    public static final String CATEGORY_KEY = "category:all";
    public static final long CATEGORY_LIST_TTL = 3600;
    public static final String COMMENT_ARTICLE_KEY = "comment:article:";
    public static final String COMMENT_ARTICLE_PAGE_KEY = ":page:";
    public static final long COMMENT_ARTICLE_TTL = 3600;
    public static final String SYS_INFO_KEY = "sys:info";
    public static final long SYS_INFO_TTL = 7200;
}
