package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.nizy.community.dto.ResultDTO;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.model.UserExample;
import top.nizy.community.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Classname AuthorizeController
 * @Description 本身网站的注册请求处理
 * @Date 2021/6/3 15:09
 * @Created by NZY271
 */
@Controller
public class RegisterController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {


        return "register";
    }

    @PostMapping("/register")
    public String registering(HttpServletRequest request, HttpServletResponse response, Model model) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String code = request.getParameter("code");
        String username = request.getParameter("username");
        Cookie[] cookies = request.getCookies();
        User user = new User();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("activeCode")) {
                String value = cookie.getValue();
                if (value.equals(code)) {
                    //说明注册成功
                    user.setStatus(1);
                    break;
                }
            }
        }
        //说明注册 成功
        if (user.getStatus() == 1) {
            user.setName(username);
            user.setPassword(password);
            user.setAccountId(email);
            user.setEmail(email);
            user.setAvatarUrl("/images/Xaccount.png");
            userService.createOrUpdate(user);
            model.addAttribute("signupSuccess", "success");
            return "login";
        } else {
            //注册失败
            return "register";
        }
    }

    @ResponseBody
    @GetMapping("/sendActiveEmail/{email}")
    public ResultDTO<Object> sendActiveEmail(@PathVariable(name = "email") String email, HttpServletResponse response) {
        //使用cookie存储验证码用于校验
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users != null && users.size() != 0) {
            return ResultDTO.errorOf(300, "Email already signup,please go to login ");
        }
        String activeCode = userService.sendEmail(email, "ActiveCode");
        Cookie cookie = new Cookie("activeCode", activeCode);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 5);
        response.addCookie(cookie);
        return ResultDTO.ok(activeCode);
    }
}
