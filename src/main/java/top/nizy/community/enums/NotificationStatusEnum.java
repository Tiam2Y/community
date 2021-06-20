package top.nizy.community.enums;

/**
 * @Classname NotificationStatusEnum
 * @Description 通知的状态枚举(未读/已读)
 * @Date 2021/6/20 16:57
 * @Created by NZY271
 */
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
