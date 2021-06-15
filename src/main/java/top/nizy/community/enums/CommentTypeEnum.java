package top.nizy.community.enums;

/**
 * @Classname CommentTypeEnum
 * @Description TODO
 * @Date 2021/6/15 16:48
 * @Created by NZY271
 */
public enum CommentTypeEnum {
    QUESTION(1),    //表示该comment是针对 Question
    COMMENT(2);     //表示该comment是针对 Comment
    private Integer type;


    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
