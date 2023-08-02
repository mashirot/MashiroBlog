package ski.mashiro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class Tag {
    private Long id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
