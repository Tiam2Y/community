package top.nizy.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.nizy.community.enums.CommentTypeEnum;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.exception.CustomizeException;
import top.nizy.community.mapper.CommentMapper;
import top.nizy.community.mapper.QuestionExtMapper;
import top.nizy.community.mapper.QuestionMapper;
import top.nizy.community.model.Comment;
import top.nizy.community.model.Question;

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
}
