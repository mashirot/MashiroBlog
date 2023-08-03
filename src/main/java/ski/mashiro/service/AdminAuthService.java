package ski.mashiro.service;

import ski.mashiro.common.Result;
import ski.mashiro.dto.AdminDTO;
import ski.mashiro.dto.AdminLoginDTO;

/**
 * @author MashiroT
 */
public interface AdminAuthService {
    /**
     * 处理用户登录
     * @param adminLoginDTO 登录DTO，包含username和password
     * @return 结果
     */
    Result<AdminDTO> login(AdminLoginDTO adminLoginDTO);

    /**
     * 用户退出，删除Redis中的缓存，达到退出效果
     * @return 结果
     */
    Result<String> logout();
}
