package ski.mashiro.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author MashiroT
 * @since 2023-08-02
 */
public class SecurityException extends AuthenticationException {
    public SecurityException(String message) {
        super(message);
    }
}
