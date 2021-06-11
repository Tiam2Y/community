package top.nizy.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;

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
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
