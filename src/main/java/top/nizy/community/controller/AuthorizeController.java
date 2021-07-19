package top.nizy.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.nizy.community.dto.UserDTO;
import top.nizy.community.model.User;
import top.nizy.community.service.UserService;
import top.nizy.community.strategy.LoginStrategyFactory;
import top.nizy.community.strategy.LoginUserInfo;
import top.nizy.community.strategy.OAuthLoginStrategy;
import top.nizy.community.utils.CookieUtils;
import top.nizy.community.utils.TokenUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Classname AuthorizeController
 * @Description 授权用户登陆的控制器 -- 第三方登录
 * @Date 2021/6/3 15:09
 * @Created by NZY271
 */

/**
 * "@Autowired" 注解 -- 自动装配 使用将 Spring 容器中的 bean 自动的和我们需要这个 bean 的类组装在一起
 */
@Controller
@Slf4j  //lombok 中用于日志追加
public class AuthorizeController {

    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private CookieUtils cookieUtils;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/callback/{type}")
    public String newCallback(@PathVariable(name = "type") String type,
                              @RequestParam(name = "code") String code,
                              @RequestParam(name = "state", required = false) String state,
                              HttpServletResponse response) {
        OAuthLoginStrategy loginStrategy = loginStrategyFactory.getStrategy(type);
        LoginUserInfo loginUserInfo = loginStrategy.getUser(code, state);
        if (loginUserInfo != null && loginUserInfo.getId() != null) {
            User user = new User();
            //修改之后此处没什么用
            String token_temp = UUID.randomUUID().toString();
            user.setToken(token_temp);
            user.setName(loginUserInfo.getName());
            user.setAccountId(String.valueOf(loginUserInfo.getId()));
            user.setType(type); //设置了用于的 type
            user.setAvatarUrl(loginUserInfo.getAvatarUrl());
            Long userId = userService.createOrUpdate(user);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setId(userId);
            Long secTime = 60 * 60 * 24 * 6L;//6天
            String token = tokenUtils.getToken(userDTO, secTime * 1000);
            Cookie cookie = cookieUtils.getCookie("token", token, secTime.intValue());
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            log.error("callback get error,{}", loginUserInfo);
            // 登录失败，重新登录
            return "redirect:/";
        }
    }
}
