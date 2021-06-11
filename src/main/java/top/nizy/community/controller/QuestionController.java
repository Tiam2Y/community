package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.nizy.community.dto.QuestionDTO;
import top.nizy.community.service.QuestionService;

/**
 * @Classname QuestionController
 * @Description TODO
 * @Date 2021/6/10 21:05
 * @Created by NZY271
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id,
                           Model model) {
        //根据 ID 获取这个问题(包含其作者信息，因此需要使用DTO)
        Long questionId = Long.parseLong(id);
        QuestionDTO questionDTO = questionService.getById(questionId);

        model.addAttribute("question", questionDTO);

        return "question";

    }
}
