package ski.mashiro.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ski.mashiro.common.Result;
import ski.mashiro.exception.SecurityException;

import static ski.mashiro.constant.StatusConstant.ACCESS_DENIED;
import static ski.mashiro.constant.StatusConstant.SYS_ERR;

/**
 * @author MashiroT
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        log.error("{}", e.getMessage());
        return Result.failed(SYS_ERR, "系统错误");
    }

    @ExceptionHandler(SecurityException.class)
    public Result<String> securityExceptionHandler(SecurityException e) {
        log.error("{}", e.getMessage());
        return Result.failed(ACCESS_DENIED, e.getMessage());
    }

}
