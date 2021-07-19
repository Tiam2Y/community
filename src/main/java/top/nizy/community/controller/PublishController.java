package top.nizy.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.nizy.community.cache.TagCache;
import top.nizy.community.dto.QuestionDTO;
import top.nizy.community.dto.UserDTO;
import top.nizy.community.model.Question;
import top.nizy.community.model.User;
import top.nizy.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname PublishController
 * @Description TODO
 * @Date 2021/6/7 14:26
 * @Created by NZY271
 */
@Controller
public class PublishController {


    @Autowired
    private QuestionService questionService;


    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model,
                       HttpServletRequest request) {


        //根据 ID 获取到这条发布信息
        QuestionDTO question = questionService.getById(id);
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");

        //拦截，不是创建者，不能修改
        if (!question.getCreator().equals(user.getId())) {
            return "redirect:/";
        }

        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());

        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }

        if (StringUtils.length(title) > 50) {
            model.addAttribute("error", "标题最多 50 个字符");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
