package ski.mashiro.exception;

public class CustomerSecurityException extends RuntimeException{
    public CustomerSecurityException(String message) {
        super(message);
    }
}
