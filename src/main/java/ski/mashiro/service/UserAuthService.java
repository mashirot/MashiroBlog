package ski.mashiro.service;

import ski.mashiro.common.Result;
import ski.mashiro.dto.AdminDTO;
import ski.mashiro.dto.AdminLoginDTO;

/**
 * @author MashiroT
 */
public interface UserAuthService {
    Result<AdminDTO> login(AdminLoginDTO adminLoginDTO);

    Result<String> logout();
}
