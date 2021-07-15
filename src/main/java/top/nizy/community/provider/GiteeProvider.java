package top.nizy.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.nizy.community.dto.AccessTokenDTO;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.exception.CustomizeException;
import top.nizy.community.provider.dto.GiteeUser;
import top.nizy.community.provider.dto.GithubUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Classname GithubProvider
 * @Description 针对 Github 的提供一些可用功能的类
 * @Date 2021/6/3 15:23
 * @Created by NZY271
 */

/**
 * "@Component"注解 -- 仅将当前类初始化至Spring容器的上下文
 * Spring IOC -- 在调用的时候不需要实例化其对象
 * 一般方法的参数超过两个以上的时候，可以考虑将其封装成对象来做
 */
@Component
public class GiteeProvider {

    @Value("${gitee.client.id}")
    private String clientId;

    @Value("${gitee.client.secret}")
    private String clientSecret;

    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    //使用 OkHttp 的POST请求处理方式
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        //使用 fastjson 将对象转换为JSON字符串
        //该方法已经过时--到时候再看
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code&code=%s&client_id=%s&redirect_uri=%s&client_secret=%s";
        url = String.format(url, accessTokenDTO.getCode(), clientId, redirectUri, clientSecret);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            JSONObject jsonObject = JSON.parseObject(string);
            String token = jsonObject.getString("access_token");
            return token;
        } catch (Exception e) {
            throw new CustomizeException(CustomizeErrorCode.OAUTH_LOGIN_TIMEOUT);
        }
    }

    //使用 OkHttp 的GET请求处理方式
    public GiteeUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        //这种登陆方式在 Gitee 中还没有过时
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //将字符串转换为JSON对象 -- fastjson
            GiteeUser giteeUser = JSON.parseObject(string, GiteeUser.class);
            return giteeUser;
        } catch (IOException e) {
            throw new CustomizeException(CustomizeErrorCode.OAUTH_FAIL);
        }
    }
}
