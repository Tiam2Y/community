package top.nizy.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @Classname TagDTO
 * @Description TODO
 * @Date 2021/6/18 10:04
 * @Created by NZY271
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
