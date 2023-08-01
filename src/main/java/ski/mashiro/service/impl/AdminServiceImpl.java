package ski.mashiro.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.Admin;
import ski.mashiro.mapper.AdminMapper;

/**
 * @author MashiroT
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
