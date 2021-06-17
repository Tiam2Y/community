package top.nizy.community.dto;

/**
 * @Classname CommentCreateDTO
 * @Description 从前端传递至后端，用于创建Comment的DTO
 * @Date 2021/6/15 14:34
 * @Created by NZY271
 */

import lombok.Data;

/**
 * 在方法的一些参数中，使用 @RequestBody 注解
 * Spring 可以将传递过来的 JSON 自动的生成相应的对象( key 和 value 相对应)
 */

@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
