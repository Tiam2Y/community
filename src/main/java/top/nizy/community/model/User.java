package top.nizy.community.model;

import lombok.Data;

/**
 * @Classname User
 * @Description TODO
 * @Date 2021/6/4 22:27
 * @Created by NZY271
 */
@Data
public class User {
    private Long id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String bio;
    private String avatarUrl;
}
