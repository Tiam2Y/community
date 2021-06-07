package top.nizy.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import top.nizy.community.model.Question;

/**
 * @Classname QuestionMapper
 * @Description TODO
 * @Date 2021/6/7 15:44
 * @Created by NZY271
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title, description, gmt_create, gmt_modified, creator, tag)" +
            "values(#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void create(Question question);
}
