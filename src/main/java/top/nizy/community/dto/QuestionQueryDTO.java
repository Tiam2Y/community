package top.nizy.community.dto;

import lombok.Data;

/**
 * @Classname QuestionQueryDTO
 * @Description TODO
 * @Date 2021/6/21 15:29
 * @Created by NZY271
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private String sort;
    private Long time;
    private String tag;
    private Integer page;
    private Integer size;
}