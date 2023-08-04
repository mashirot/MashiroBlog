package ski.mashiro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ski.mashiro.entity.Admin;
import ski.mashiro.service.AdminService;

import java.time.LocalDateTime;

@SpringBootTest
class MashiroBlogApplicationTests {

    @Test
    void insertUser(@Autowired AdminService adminService, @Autowired PasswordEncoder passwordEncoder) {
        adminService.save(new Admin(
                null,
                "mashiro",
                passwordEncoder.encode("123456"),
                "Shiina",
                "ShiinaMashiro@sakurasou.com",
                "ねぇ，あなたは何色になりたい?",
                LocalDateTime.now()
        ));
    }

}
