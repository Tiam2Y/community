package top.nizy.community.enums;

/**
 * @Classname NotificationEnum
 * @Description 通知的类型
 * @Date 2021/6/20 15:18
 * @Created by NZY271
 */
public enum NotificationEnum {
    REPLY_TOPIC(1, "回复了您的帖子"),
    REPLY_COMMENT(2, "回复了您的评论");
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
