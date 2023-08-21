package ski.mashiro.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.mashiro.common.Result;
import ski.mashiro.dto.SysInfoDTO;
import ski.mashiro.entity.SysInfo;
import ski.mashiro.service.SysInfoService;
import ski.mashiro.util.RedisUtils;

import java.util.concurrent.TimeUnit;

import static ski.mashiro.constant.RedisConsts.SYS_INFO_KEY;
import static ski.mashiro.constant.RedisConsts.SYS_INFO_TTL;
import static ski.mashiro.constant.StatusConsts.SYS_INFO_SUCCESS;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/info")
public class SysInfoController {

    private final SysInfoService sysInfoService;
    private final RedisUtils redisUtils;

    public SysInfoController(SysInfoService sysInfoService, RedisUtils redisUtils) {
        this.sysInfoService = sysInfoService;
        this.redisUtils = redisUtils;
    }

    @GetMapping
    public Result<SysInfoDTO> info() throws JsonProcessingException {
        SysInfo sysInfo = redisUtils.getOrSetCache(SYS_INFO_KEY, "", SysInfo.class, SYS_INFO_TTL, TimeUnit.SECONDS, (ignore) -> sysInfoService.list().get(0));
        SysInfoDTO sysInfoDTO = new SysInfoDTO(sysInfo.getOwnerNickname(), DigestUtils.md5DigestAsHex(sysInfo.getOwnerEmail().getBytes()), sysInfo.getOwnerProfile(), sysInfo.getRunDay());
        return Result.success(SYS_INFO_SUCCESS, sysInfoDTO);
    }

    @GetMapping("/detail")
    public Result<SysInfo> detail() {
        return Result.success(SYS_INFO_SUCCESS, sysInfoService.list().get(0));
    }
}
