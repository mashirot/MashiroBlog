package ski.mashiro.entity;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class SysInfo {
    private String ownerNickname;
    private String ownerEmail;
    private String ownerProfile;
    private Long articleCount;
    private Long commentCount;
    private Long runDay;
}
