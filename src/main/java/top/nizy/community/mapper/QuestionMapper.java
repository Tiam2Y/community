package top.nizy.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.nizy.community.model.Question;

import java.util.List;

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

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select count(1) from question where creator=#{userId}")
    Integer countByCreator(@Param(value = "userId") Long userId);

    @Select("select * from question where creator=#{userId} limit #{offset}, #{size}")
    List<Question> listByCreator(@Param(value = "userId") Long userId,
                                 @Param(value = "offset") Integer offset,
                                 @Param(value = "size") Integer size);
}
