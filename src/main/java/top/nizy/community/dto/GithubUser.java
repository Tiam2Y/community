package top.nizy.community.dto;

import lombok.Data;

/**
 * @Classname GithubUser
 * @Description TODO
 * @Date 2021/6/3 16:34
 * @Created by NZY271
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
