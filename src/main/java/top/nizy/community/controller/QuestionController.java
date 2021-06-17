package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.nizy.community.dto.CommentDTO;
import top.nizy.community.dto.QuestionDTO;
import top.nizy.community.enums.CommentTypeEnum;
import top.nizy.community.service.CommentService;
import top.nizy.community.service.QuestionService;

import java.util.List;

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
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id,
                           Model model) {
        //根据 ID 获取这个问题(包含其作者信息，因此需要使用DTO)
        Long questionId = Long.parseLong(id);
        //没有获取到这个数据时会 throw 异常
        QuestionDTO questionDTO = questionService.getById(questionId);
        //获取与这个问题的标签相关的相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        //获取这个问题下的所有评论(一级)
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);
        //增加阅读数
        questionService.incView(questionId);

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);

        return "question";
    }
}
