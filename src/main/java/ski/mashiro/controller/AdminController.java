package ski.mashiro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.JwtInfo;
import ski.mashiro.common.Result;
import ski.mashiro.dto.AdminDTO;
import ski.mashiro.dto.AdminInfoDTO;
import ski.mashiro.dto.AdminLoginDTO;
import ski.mashiro.dto.AdminUpdateDTO;
import ski.mashiro.entity.Admin;
import ski.mashiro.service.AdminAuthService;
import ski.mashiro.service.AdminService;
import ski.mashiro.util.RedisUtils;

import java.time.LocalDateTime;

import static ski.mashiro.constant.RedisConsts.SYS_INFO_KEY;
import static ski.mashiro.constant.StatusConsts.*;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminAuthService adminAuthService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtils redisUtils;

    public AdminController(AdminAuthService adminAuthService, AdminService adminService, PasswordEncoder passwordEncoder, RedisUtils redisUtils) {
        this.adminAuthService = adminAuthService;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.redisUtils = redisUtils;
    }

    /**
     * 仅能请求一次
     * @return 结果
     */
    @PostMapping("/reg")
    public Result<String> reg(@RequestBody Admin admin) {
        if (adminService.count() > 0) {
            return Result.failed(REG_FAILED, "已经存在管理员用户");
        }
        admin.setId(null);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setCreateTime(LocalDateTime.now());
        if (adminService.save(admin)) {
            return Result.success(REG_SUCCESS, "注册成功");
        }
        return Result.failed(REG_FAILED, "请检查用户约束条件是否满足");
    }

    @PostMapping("/login")
    public Result<AdminDTO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        return adminAuthService.login(adminLoginDTO);
    }

    @GetMapping("/logout")
    public Result<String> logout() {
        return adminAuthService.logout();
    }

    @PutMapping
    public Result<String> update(@RequestBody AdminUpdateDTO adminUpdateDTO) {
        JwtInfo jwtInfo = (JwtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var updateWrapper = new LambdaUpdateWrapper<Admin>()
                .set(StringUtils.hasText(adminUpdateDTO.getPassword()), Admin::getPassword, passwordEncoder.encode(adminUpdateDTO.getPassword()))
                .set(StringUtils.hasText(adminUpdateDTO.getNickname()), Admin::getNickname, adminUpdateDTO.getNickname())
                .set(StringUtils.hasText(adminUpdateDTO.getEmail()), Admin::getEmail, adminUpdateDTO.getEmail())
                .set(StringUtils.hasText(adminUpdateDTO.getProfile()), Admin::getProfile, adminUpdateDTO.getProfile())
                .eq(Admin::getId, jwtInfo.id());
        redisUtils.delete(SYS_INFO_KEY);
        return adminService.update(updateWrapper) ? Result.success(ADMIN_UPDATE_SUCCESS, null) : Result.failed(ADMIN_UPDATE_FAILED, "更新失败，请检查后端日志");
    }

    @GetMapping
    public Result<AdminInfoDTO> info() {
        JwtInfo jwtInfo = (JwtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId, jwtInfo.id()));
        return Result.success(ADMIN_INFO_SUCCESS, new AdminInfoDTO(admin.getId(), admin.getUsername(), admin.getNickname(), admin.getEmail(), admin.getProfile(), admin.getCreateTime()));
    }
}
