package top.nizy.community.dto;

/**
 * @Classname CommentDTO
 * @Description 前端和后端之间传输
 * @Date 2021/6/15 14:34
 * @Created by NZY271
 */

import lombok.Data;

/**
 * 在方法的一些参数中，使用 @RequestBody 注解
 * Spring 可以将传递过来的 JSON 自动的生成相应的对象( key 和 value 相对应)
 */

@Data
public class CommentDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
