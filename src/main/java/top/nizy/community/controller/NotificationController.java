package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.nizy.community.dto.NotificationDTO;
import top.nizy.community.enums.NotificationTypeEnum;
import top.nizy.community.model.User;
import top.nizy.community.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname NotificationController
 * @Description TODO
 * @Date 2021/6/20 17:38
 * @Created by NZY271
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    /**
     * 作为点击通知时的一个中转，进行一些验证，然后跳转至相应帖子
     */
    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterId();
        } else {
            return "redirect:/";
        }
    }
}
