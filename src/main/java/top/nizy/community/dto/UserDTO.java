package top.nizy.community.dto;

import lombok.Data;

/**
 * @Classname UserDTO
 * @Description TODO
 * @Date 2021/7/19 14:09
 * @Created by NZY271
 */
@Data
public class UserDTO {
    private Long id;
    private String accountId;
    private String name;
    private String avatarUrl;
}
