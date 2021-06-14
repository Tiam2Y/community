package top.nizy.community.exception;

/**
 * @Classname CustomizeException
 * @Description 这个 package 存放自定义的异常
 * @Date 2021/6/14 21:34
 * @Created by NZY271
 */
public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
