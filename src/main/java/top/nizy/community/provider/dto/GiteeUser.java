package top.nizy.community.provider.dto;

import lombok.Data;

/**
 * @Classname GiteeUser
 * @Description TODO
 * @Date 2021/6/3 16:34
 * @Created by NZY271
 */
@Data
public class GiteeUser {
    private String name;
    private Long id;    //这个Github的ID是唯一的
    private String avatarUrl;
}
