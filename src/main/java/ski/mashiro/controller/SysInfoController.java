package ski.mashiro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.mashiro.common.Result;
import ski.mashiro.entity.SysInfo;
import ski.mashiro.service.SysInfoService;

import static ski.mashiro.constant.StatusConstant.SYS_INFO_SUCCESS;

@RestController
@RequestMapping("/info")
public class SysInfoController {

    private final SysInfoService sysInfoService;

    public SysInfoController(SysInfoService sysInfoService) {
        this.sysInfoService = sysInfoService;
    }

    @GetMapping
    public Result<SysInfo> info() {
        return Result.success(SYS_INFO_SUCCESS, sysInfoService.list().get(0));
    }
}
