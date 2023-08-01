package ski.mashiro.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ski.mashiro.common.JwtInfo;
import ski.mashiro.common.Result;
import ski.mashiro.dto.AdminDTO;
import ski.mashiro.dto.AdminLoginDTO;
import ski.mashiro.entity.Admin;
import ski.mashiro.security.SecurityUser;
import ski.mashiro.service.UserAuthService;
import ski.mashiro.utils.JwtUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static ski.mashiro.constant.RedisConstant.USER_JWT_KEY;
import static ski.mashiro.constant.StatusConstant.*;

/**
 * @author MashiroT
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate redisTemplate;

    public UserAuthServiceImpl(AuthenticationManager authenticationManager, StringRedisTemplate redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Result<AdminDTO> login(AdminLoginDTO adminLoginDTO) {
        if (!StringUtils.hasText(adminLoginDTO.getUsername()) || !StringUtils.hasText(adminLoginDTO.getPassword())) {
            return Result.failed(LOGIN_FAILED, "用户名或密码不得为空");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(adminLoginDTO.getUsername(), adminLoginDTO.getPassword());
        Authentication authenticated = authenticationManager.authenticate(authenticationToken);
        if (!authenticated.isAuthenticated()) {
            return Result.failed(LOGIN_FAILED, "用户名或密码错误");
        }
        SecurityUser securityUser = (SecurityUser) authenticated.getPrincipal();
        Admin admin = securityUser.getAdmin();
        String jwtId = UUID.randomUUID().toString();
        String jwt = JwtUtils.createJwt(Map.of("jwtId", jwtId, "id", admin.getId(), "username", admin.getUsername()));
        redisTemplate.opsForValue().set(USER_JWT_KEY + jwtId, "", JwtUtils.EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return Result.success(LOGIN_SUCCESS, new AdminDTO(admin.getId(), admin.getUsername(), admin.getEmail(), jwt));
    }

    @Override
    public Result<String> logout() {
        JwtInfo jwtInfo = (JwtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisTemplate.delete(USER_JWT_KEY + jwtInfo.jwtId());
        return Result.success(LOGOUT_SUCCESS, "退出成功");
    }
}
