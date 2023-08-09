package ski.mashiro.dto;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class SysInfoDTO {
    private String ownerNickname;
    private String ownerEmail;
    private String ownerProfile;
    private Long articleCount;
    private Long commentCount;
    private Long runDay;
}
