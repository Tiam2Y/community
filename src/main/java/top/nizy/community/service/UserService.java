package top.nizy.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.model.UserExample;

import java.util.List;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2021/6/10 22:30
 * @Created by NZY271
 */
@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //获取数据库中的 user
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);

        if (dbUsers.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser = dbUsers.get(0);   //用于获取需要更新的数据在数据库中的ID(即where语句)
            //创建需要更新的内容，不提供的默认不更新(即为null的)
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            //创建更新时的SQL语句
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }
}
