package ski.mashiro.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.Admin;
import ski.mashiro.exception.SecurityException;
import ski.mashiro.security.SecurityUser;
import ski.mashiro.service.AdminService;

import java.util.Objects;

/**
 * @author MashiroT
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final AdminService adminService;

    public UserDetailServiceImpl(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (Objects.isNull(admin)) {
            throw new SecurityException("用户不存在");
        }
        return new SecurityUser(admin);
    }
}
