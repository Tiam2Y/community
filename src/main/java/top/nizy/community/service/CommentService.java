package top.nizy.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.nizy.community.dto.CommentDTO;
import top.nizy.community.enums.CommentTypeEnum;
import top.nizy.community.enums.NotificationStatusEnum;
import top.nizy.community.enums.NotificationTypeEnum;
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
    private UserMapper userMapper;

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required = false)
    private CommentExtMapper commentExtMapper;


    //用来处理数据库事务的Spring注解 -- 可以注解在任何方法前
    //默认在出现任何异常时 回滚
    @Transactional
    public void insert(Comment comment, User creator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            /**
             * 回复评论
             */
            // 在数据库中找到目标 "评论"
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //再在数据库中找到这些评论所在的问题 -- 主要用于显示通知时使用
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            //更新相应的回复数量
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

            // 创建通知
//            createNotify(comment, dbComment.getCommentator(), creator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
            createNotify(comment, dbComment.getCommentator(), creator.getName(), NotificationTypeEnum.REPLY_COMMENT, question);
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

            // 创建通知
//            createNotify(comment, question.getCreator(), creator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
            createNotify(comment, question.getCreator(), creator.getName(), NotificationTypeEnum.REPLY_QUESTION, question);
        }
    }

    /**
     * 在向数据库插入新的 comment 时 需要创建通知
     */
    private void createNotify(Comment comment, Long receiver, String notifierName, NotificationTypeEnum notificationType, Question question) {
        //测试的时候把这块删了
        if (receiver.equals(comment.getCommentator())) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(question.getId());   //这个通知所链接的问题的ID
        notification.setNotifier(comment.getCommentator());
        //默认未读状态
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(question.getTitle());
        notificationMapper.insert(notification);
    }
//    /**
//     * 在向数据库插入新的 comment 时 需要创建通知
//     */
//    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
//        //测试的时候把这块删了
//        if (receiver.equals(comment.getCommentator())) {
//            return;
//        }
//        Notification notification = new Notification();
//        notification.setGmtCreate(System.currentTimeMillis());
//        notification.setType(notificationType.getType());
//        notification.setOuterId(outerId);   //这个通知所链接的问题的ID
//        notification.setNotifier(comment.getCommentator());
//        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
//        notification.setReceiver(receiver);
//        notification.setNotifierName(notifierName);
//        notification.setOuterTitle(outerTitle);
//        notificationMapper.insert(notification);
//    }

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
