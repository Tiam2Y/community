package top.nizy.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.nizy.community.dto.CommentCreateDTO;
import top.nizy.community.dto.CommentDTO;
import top.nizy.community.dto.ResultDTO;
import top.nizy.community.enums.CommentTypeEnum;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.model.Comment;
import top.nizy.community.model.User;
import top.nizy.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 接收从前端传递过来的JSON字符串，将其转换为相应对象
     * 然后向数据库中插入相应内容
     * 既能接收 问题下的初级评论，也能接收评论下的评论(根据commentCreateDTO中的type决定)
     *
     * @param commentCreateDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    // @RequestBody 不能省略，会自动将字符串反序列化为JSON？再转换为Java对象？
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment, user);
        return ResultDTO.ok();
    }

    /**
     * 将二级评论封装为 JSON 传递至前端，前端利用这些数据进行展示
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.ok(commentDTOS);
    }
}
