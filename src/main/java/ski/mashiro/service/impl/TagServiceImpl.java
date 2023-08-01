package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ski.mashiro.entity.Tag;
import ski.mashiro.mapper.TagMapper;
import ski.mashiro.service.TagService;

/**
 * @author MashiroT
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}
