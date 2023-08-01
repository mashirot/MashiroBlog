package ski.mashiro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class Admin {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private LocalDateTime createTime;

    public Admin(String username, String password, String nickname, String email, LocalDateTime createTime) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.createTime = createTime;
    }
}
