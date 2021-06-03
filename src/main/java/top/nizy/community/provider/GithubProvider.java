package top.nizy.community.provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import top.nizy.community.dto.AccessTokenDTO;
import top.nizy.community.dto.GithubUser;

import java.io.IOException;

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
    //使用 OkHttp 的POST请求处理方式
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
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
            e.printStackTrace();
        }
        return null;
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
        }
        return null;
    }
}
