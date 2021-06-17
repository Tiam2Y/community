package top.nizy.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.nizy.community.dto.CommentDTO;
import top.nizy.community.enums.CommentTypeEnum;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.exception.CustomizeException;
import top.nizy.community.mapper.*;
import top.nizy.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Classname CommentService
 * @Description TODO
 * @Date 2021/6/15 16:50
 * @Created by NZY271
 */
@Service
public class CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required = false)
    private CommentExtMapper commentExtMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    //用来处理数据库事务的Spring注解 -- 可以注解在任何方法前
    //默认在出现任何异常时 回滚
    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            /**
             * 回复评论
             */
            // 在数据库中找到目标 "评论"
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

//            // 回复问题
//            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
//            if (question == null) {
//                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
//            }

            commentMapper.insert(comment);
            //更新相应的回复数量
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

//            // 增加评论数
//            Comment parentComment = new Comment();
//            parentComment.setId(comment.getParentId());
//            parentComment.setCommentCount(1);
//            commentExtMapper.incCommentCount(parentComment);
//
//            // 创建通知
//            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        } else {
            /**
             * 回复问题
             */
            // 在数据库中找到目标 "问题"
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
//            comment.setCommentCount(0);
            //可能存在事务要求——整个事务回滚/提交
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

//            // 创建通知
//            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }

    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc"); //按创建时间的倒序排序
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论人, 并将其转换为列表
        List<Long> userIds = comments.stream().map(Comment::getCommentator).distinct().collect(Collectors.toList());

        // 获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);  //在评论人中的User
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOs;
    }
}
