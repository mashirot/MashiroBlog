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

import static ski.mashiro.constant.StatusConstant.*;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminAuthService adminAuthService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(AdminAuthService adminAuthService, AdminService adminService, PasswordEncoder passwordEncoder) {
        this.adminAuthService = adminAuthService;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
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
        return adminService.update(updateWrapper) ? Result.success(ADMIN_UPDATE_SUCCESS, null) : Result.failed(ADMIN_UPDATE_FAILED, "更新失败，请检查后端日志");
    }

    @GetMapping
    public Result<AdminInfoDTO> info() {
        JwtInfo jwtInfo = (JwtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId, jwtInfo.id()));
        return Result.success(ADMIN_INFO_SUCCESS, new AdminInfoDTO(admin.getId(), admin.getUsername(), admin.getNickname(), admin.getEmail(), admin.getProfile(), admin.getCreateTime()));
    }
}
