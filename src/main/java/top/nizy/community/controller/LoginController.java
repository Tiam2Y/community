package top.nizy.community.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import top.nizy.community.dto.UserDTO;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.model.UserExample;
import top.nizy.community.service.NotificationService;
import top.nizy.community.utils.CookieUtils;
import top.nizy.community.utils.TokenUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * @Classname AuthorizeController
 * @Description 本身网站的通过注册的用户的登陆
 * @Date 2021/6/3 15:09
 * @Created by NZY271
 */

@Controller
public class LoginController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CookieUtils cookieUtils;

    @GetMapping("/login")
    public String LoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String Login(HttpServletRequest request, HttpServletResponse response, Model model) {
        String account = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberFlag = request.getParameter("rememberFlag");
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(account)
                .andPasswordEqualTo(password);

        List<User> users = userMapper.selectByExample(userExample);
        if (users != null && users.size() != 0) {
            User user = users.get(0);
            String token_temp = UUID.randomUUID().toString();
            user.setToken(token_temp);
            userMapper.updateByPrimaryKey(user);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            Long secTime = 60 * 60 * 24 * 6L;//6天
            String token = tokenUtils.getToken(userDTO, secTime * 1000);
            Cookie cookie = cookieUtils.getCookie("token", token, secTime.intValue());
            if(rememberFlag==null)
                cookie.setMaxAge(-1);//没有则是默认
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            model.addAttribute("loginFail", "fail");
            return "login";
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
