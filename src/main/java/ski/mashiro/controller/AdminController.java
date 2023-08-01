package ski.mashiro.controller;

import org.springframework.web.bind.annotation.*;
import ski.mashiro.common.Result;
import ski.mashiro.dto.AdminDTO;
import ski.mashiro.dto.AdminLoginDTO;
import ski.mashiro.service.UserAuthService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserAuthService userAuthService;

    public AdminController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public Result<AdminDTO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        return userAuthService.login(adminLoginDTO);
    }

    @GetMapping("/logout")
    public Result<String> logout() {
        return userAuthService.logout();
    }
}
