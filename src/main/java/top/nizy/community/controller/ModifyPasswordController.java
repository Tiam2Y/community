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
 * @Classname ModifyPasswordController
 * @Description TODO
 * @Date 2021/7/1 13:43
 * @Created by NZY271
 */
@Controller
public class ModifyPasswordController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/modify")
    public String modifyPage() {
        return "modifyPassword";
    }

    @PostMapping("/modify")
    public String modify(HttpServletRequest request, HttpServletResponse response, Model model) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String code = request.getParameter("code");
        Cookie[] cookies = request.getCookies();
        User user = new User();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("modifyCode")) {
                String value = cookie.getValue();
                if (value.equals(code)) {
                    user.setStatus(1);
                    break;
                }
            }
        }
        if (user.getStatus() == 1) {
            UserExample example = new UserExample();
            example.createCriteria()
                    .andAccountIdEqualTo(email);
            User modifiedUser = userMapper.selectByExample(example).get(0);
            modifiedUser.setPassword(password);
            userMapper.updateByPrimaryKey(modifiedUser);
            return "login";
        } else {
            //????????????
            model.addAttribute("modifyFail", "fail");
            return "modifyPassword";
        }
    }


    @ResponseBody
    @GetMapping("/sendModifyEmail/{email}")
    public ResultDTO sendModifyEmail(@PathVariable(name = "email") String email, HttpServletResponse response) {
        //??????cookie???????????????????????????
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users == null || users.size() == 0) {
            return ResultDTO.errorOf(400, "User is not found ");
        }
        String modifyCode = userService.sendEmail(email, "modifyCode");
        Cookie cookie = new Cookie("modifyCode", modifyCode);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 5);
        response.addCookie(cookie);
        return ResultDTO.ok(modifyCode);
    }
}
