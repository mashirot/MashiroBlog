package ski.mashiro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String profile;
    private LocalDateTime createTime;
}
