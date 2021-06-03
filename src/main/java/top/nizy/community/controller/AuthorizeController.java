package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.nizy.community.dto.AccessTokenDTO;
import top.nizy.community.dto.GithubUser;
import top.nizy.community.provider.GithubProvider;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        //此处设置的是 绑定的这个 Application 的所有者的信息
        accessTokenDTO.setClient_id("ad3aafde0ac9783ef322");
        accessTokenDTO.setClient_secret("c79b8d9d78044071cd922e4da41c0f48542919c4");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri("http://localhost:8686/callback");
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        //希望登陆成功后，重新返回index页面
        return "index";
    }

}
