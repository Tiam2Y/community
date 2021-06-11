package top.nizy.community.dto;

import lombok.Data;
import top.nizy.community.model.User;

/**
 * @Classname QuestionDTO
 * @Description TODO
 * @Date 2021/6/7 20:57
 * @Created by NZY271
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
