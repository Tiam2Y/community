package top.nizy.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.nizy.community.model.User;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2021/6/4 22:21
 * @Created by NZY271
 */

/**
 * 网络之间传输的数据 称之为 dto 放在 dto 包下
 * 数据库中的数据 称之为 model 放在 model 包下
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified, avatar_url) " +
            "values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token=#{token}")
    User findByCookie(@Param("token") String token);

    @Select("select * from user where id=#{id}")
    User findByID(@Param("id")Long id);
}
