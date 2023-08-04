package ski.mashiro.dto;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class AdminUpdateDTO {
    private String password;
    private String nickname;
    private String email;
    private String profile;
}
