package top.nizy.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import top.nizy.community.model.Question;
import top.nizy.community.model.QuestionExample;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
}