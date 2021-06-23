package top.nizy.community.exception;

/**
 * @Classname CustomizeErrorCode
 * @Description 对一些自定义的异常的错误信息 message 以及 stateCode进行封装
 * @Date 2021/6/14 21:42
 * @Created by NZY271
 */

/**
 * 封装为枚举类 -- 为了避免自定义异常类太多而导致类爆炸(太多了，封装)
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "你找到问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "回复目标不存在"),
    NO_LOGIN(2003, "当前操作需要登录，请登陆后重试"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "不能读取别人的通知"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    QUESTION_DELETED(2010, "帖子已删除，或您无权删除"),
    FILE_UPLOAD_FAIL(2011, "图片上传失败"),
//    INVALID_INPUT(2011, "非法输入"),
//    INVALID_OPERATION(2012, "兄弟，是不是走错房间了？"),
    ;

    private Integer code;

    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}

