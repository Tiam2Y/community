package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.nizy.community.dto.CommentDTO;
import top.nizy.community.dto.ResultDTO;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.mapper.CommentMapper;
import top.nizy.community.model.Comment;
import top.nizy.community.model.User;
import top.nizy.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Classname CommentController
 * @Description 处理回复
 * @Date 2021/6/15 11:21
 * @Created by NZY271
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(1L);
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.ok();
    }
}
