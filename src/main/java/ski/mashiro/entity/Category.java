package ski.mashiro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
