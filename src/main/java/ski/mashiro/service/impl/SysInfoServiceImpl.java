package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.SysInfo;
import ski.mashiro.mapper.SysInfoMapper;
import ski.mashiro.service.SysInfoService;

/**
 * @author MashiroT
 */
@Service
public class SysInfoServiceImpl extends ServiceImpl<SysInfoMapper, SysInfo> implements SysInfoService {
}
