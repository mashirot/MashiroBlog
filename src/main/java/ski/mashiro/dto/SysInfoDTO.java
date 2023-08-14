package ski.mashiro.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysInfoDTO {
    private String ownerNickname;
    private String ownerEmailMD5;
    private String ownerProfile;
    private Long runDay;
}
