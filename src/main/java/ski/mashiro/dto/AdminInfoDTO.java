package ski.mashiro.dto;

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
public class AdminInfoDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String profile;
    private LocalDateTime createTime;
}
