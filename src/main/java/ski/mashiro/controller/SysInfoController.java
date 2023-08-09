package ski.mashiro.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.mashiro.common.Result;
import ski.mashiro.dto.SysInfoDTO;
import ski.mashiro.entity.SysInfo;
import ski.mashiro.service.SysInfoService;

import static ski.mashiro.constant.StatusConstant.SYS_INFO_SUCCESS;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/info")
public class SysInfoController {

    private final SysInfoService sysInfoService;

    public SysInfoController(SysInfoService sysInfoService) {
        this.sysInfoService = sysInfoService;
    }

    @GetMapping
    public Result<SysInfoDTO> info() {
        SysInfo sysInfo = sysInfoService.list().get(0);
        SysInfoDTO sysInfoDTO = new SysInfoDTO();
        BeanUtils.copyProperties(sysInfo, sysInfoDTO);
        return Result.success(SYS_INFO_SUCCESS, sysInfoDTO);
    }

    @GetMapping("/detail")
    public Result<SysInfo> detail() {
        return Result.success(SYS_INFO_SUCCESS, sysInfoService.list().get(0));
    }
}
