package top.nizy.community.strategy;

import lombok.Data;

/**
 * @Classname LoginUserInfo
 * @Description 第三方登陆用户需要提供的信息
 * @Date 2021/7/15 10:50
 * @Created by NZY271
 */
@Data
public class LoginUserInfo {
    private String name;
    private Long id;    //授权方的唯一ID，用户列表中的 type 用来区分
    private String avatarUrl;
}
