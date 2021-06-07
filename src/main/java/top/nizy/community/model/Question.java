package top.nizy.community.model;

import lombok.Data;

/**
 * @Classname Question
 * @Description TODO
 * @Date 2021/6/7 15:48
 * @Created by NZY271
 */
@Data
public class Question {
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
}