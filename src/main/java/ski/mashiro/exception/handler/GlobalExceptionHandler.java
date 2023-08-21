package ski.mashiro.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ski.mashiro.common.Result;

import static ski.mashiro.constant.StatusConsts.ACCESS_DENIED;
import static ski.mashiro.constant.StatusConsts.SYS_ERR;

/**
 * @author MashiroT
 * @since 2023-08-02
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        log.error("{}: {}", e.getClass(), e.getMessage());
        log.error("{}", e.getCause().getMessage());
        return Result.failed(SYS_ERR, "系统错误");
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<String> securityExceptionHandler(AuthenticationException e) {
        log.error("{}: {}", e.getClass(), e.getMessage());
        return Result.failed(ACCESS_DENIED, e.getMessage());
    }

}
