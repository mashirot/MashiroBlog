package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.Admin;
import ski.mashiro.mapper.AdminMapper;
import ski.mashiro.service.AdminService;

/**
 * @author MashiroT
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
