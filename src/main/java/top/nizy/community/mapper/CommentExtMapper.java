package top.nizy.community.mapper;

import top.nizy.community.model.Comment;


public interface CommentExtMapper {
    int incCommentCount(Comment record);
}