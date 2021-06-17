package top.nizy.community.dto;

import lombok.Data;
import top.nizy.community.model.User;

/**
 * @Classname CommentDTO
 * @Description 用于保存在服务端，并且传输给前端的查询到的Comment的DTO
 * @Date 2021/6/16 16:15
 * @Created by NZY271
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
