package ski.mashiro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author MashiroT
 */
@Data
public class Article {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    @TableField(exist = false)
    private List<String> category;
    @TableField(exist = false)
    private List<String> tag;
    /**
     * 0:正常 1:删除
     */
    @TableField("is_delete")
    private Boolean deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
