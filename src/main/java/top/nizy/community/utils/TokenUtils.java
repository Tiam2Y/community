package top.nizy.community.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.nizy.community.dto.ResultDTO;
import top.nizy.community.dto.UserDTO;
import top.nizy.community.exception.CustomizeErrorCode;

import java.util.Date;
import java.util.Map;

/**
 * @Classname TokenUtils
 * @Description TODO
 * @Date 2021/7/19 14:29
 * @Created by NZY271
 */
@Component
public class TokenUtils {
    @Value("${site.jwt.secret}")
    private String SECRET;



    public String getToken(UserDTO user, Long ms) {
        String token = "";
        token = JWT.create()
                .withIssuer("MovieTalk")    //token的发布者
                .withClaim("name", user.getName())  //以下是自定义声明
                .withClaim("id", user.getId())
                .withClaim("avatarUrl", user.getAvatarUrl())
                .withExpiresAt(new Date(System.currentTimeMillis() + ms)) //保质期毫秒级
                .sign(Algorithm.HMAC256(SECRET));// 密钥
        return token;
    }

    public ResultDTO<Object> verifyToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer("MovieTalk").build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            Map<String, Claim> map = verify.getClaims();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(map.get("id").asLong());
            userDTO.setName(map.get("name").asString());
            userDTO.setAvatarUrl(map.get("avatarUrl").asString());
            return ResultDTO.ok(userDTO);
        } catch (JWTVerificationException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
    }
}
