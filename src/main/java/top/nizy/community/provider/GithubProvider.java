package top.nizy.community.provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.nizy.community.dto.AccessTokenDTO;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.exception.CustomizeException;
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
public class GithubProvider {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    //使用 OkHttp 的POST请求处理方式
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {

        //传入的AccessTokenDTO还差点特定第三方网站需要的信息
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        //OkHttpClient client = new OkHttpClient();
        //Github 访问困难，存在超时会连接的失败问题
        //在创建时使用构建器设置超时时间--并没有实际多大用处
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        //使用 fastjson 将对象转换为JSON字符串
        //该方法已经过时--到时候再看
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            //上面String的结果类似如下，需要进行分隔，出 access_token
            //access_token=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&scope=user&token_type=bearer
            //System.out.println(string);
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.OAUTH_LOGIN_TIMEOUT);
        }
    }

    //使用 OkHttp 的GET请求处理方式
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        /*//这种登陆方式过时了
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();*/
        //最新的登陆方式如下
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            //将字符串转换为JSON对象 -- fastjson
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            throw new CustomizeException(CustomizeErrorCode.OAUTH_FAIL);
        }
    }
}
