package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.nizy.community.dto.QuestionDTO;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Classname IndexController
 * @Date 2021/6/1 16:44
 * @Created by NZY271
 */

/**
 * "@Controller"注解 -- 将当前类作为路由API的承载者
 */
@Controller
public class IndexController {

    @Autowired(required = false)
    private UserMapper userMapper;  //可以操作数据库

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model) {
        //需要获取 Cookie 来判断用户是否是已登录的
        //使用 request 来获取 Cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByCookie(token);
                    if (user != null)
                        //说明数据库保留了该登陆过的用户
                        //在Session中写入该用户信息--在页面展示
                        request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questions", questionList);
        return "index";
    }
}
