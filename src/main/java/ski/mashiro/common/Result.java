package ski.mashiro.common;

/**
 * @param code 状态码
 * @param data 返回数据
 * @param msg  响应消息
 * @param <T>  数据类型
 * @author MashiroT
 * @since 2022-06-28
 */
public record Result<T>(Integer code, T data, String msg) {
    public static <T> Result<T> success(Integer code, T data) {
        return new Result<>(code, data, null);
    }

    public static <T> Result<T> failed(Integer code, String msg) {
        return new Result<>(code, null, msg);
    }
}
