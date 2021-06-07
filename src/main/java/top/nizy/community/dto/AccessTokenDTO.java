package top.nizy.community.dto;

import lombok.Data;


/**
 * @Classname AccessTokenDTO
 * @Description dto -- data transform object 数据传输对象
 * @Date 2021/6/3 15:29
 * @Created by NZY271
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
