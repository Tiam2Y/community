package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.nizy.community.dto.AccessTokenDTO;
import top.nizy.community.dto.GithubUser;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.provider.GithubProvider;
import top.nizy.community.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * @Classname AuthorizeController
 * @Description TODO
 * @Date 2021/6/3 15:09
 * @Created by NZY271
 */

/**
 * "@Autowired" 注解 -- 自动装配 使用将 Spring 容器中的 bean 自动的和我们需要这个 bean 的类组装在一起
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        //此处设置的是 绑定的这个 Application 的所有者的信息
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId() != null) {
            //登陆成功
            User user = new User();
            //自定义生成一个 token -- 然后将其放入 Cookies 中
            //作为之后持久化登陆的一个验证(口令)
            String token = UUID.randomUUID().toString();
            //将登陆的用户信息 存入数据库中(因此不用做session？)
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //在数据库中创建或者修改用户信息
            userService.createOrUpdate(user);

            //响应
            Cookie cookie = new Cookie("token", token);
            //设置 Cookie 生命周期 -- 单位 s -- 如下设置为1个月
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);//--通知客户端保存 Cookies
            return "redirect:/";
        } else {
            //登陆失败，重新登陆
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        //删除session中的特定信息
        request.getSession().removeAttribute("user");
        //删除特定的 Cookie
        Cookie token = new Cookie("token", null);
        token.setMaxAge(0); //立即删除
        response.addCookie(token);
        return "redirect:/";
    }

}
