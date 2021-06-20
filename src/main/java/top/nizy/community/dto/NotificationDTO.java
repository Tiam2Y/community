package top.nizy.community.dto;

import lombok.Data;

/**
 * @Classname NotificationDTO
 * @Description 用于向前端传输的通知的信息 -- 只与当前登陆用户有关
 * @Date 2021/6/20 16:30
 * @Created by NZY271
 */
@Data
public class NotificationDTO {
    private Long id;    //数据库中的通知的ID
    private Long gmtCreate; //通知的创建时间
    private Integer status; //通知的状态(已读/未读)
    private Long notifier;  //通知的发送者(即问题/评论下的评论者)
    private String notifierName;
    private String outerTitle;
    private Long outerId;   //与此通知相关联的帖子ID及Title
    //通知的内容是与问题/评论相关
    private String typeName;
    private Integer type;
}
