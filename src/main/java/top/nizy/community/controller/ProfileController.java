package top.nizy.community.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.nizy.community.dto.PaginationDTO;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname ProfileController
 * @Description TODO
 * @Date 2021/6/9 16:30
 * @Created by NZY271
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    //{ } 内的内容+@PathVAriable()
    //可以实现动态切换路径
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        //希望可以根据用户查到与其相关的问题
        PaginationDTO pagination = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination", pagination);
        return "profile";
    }
}
