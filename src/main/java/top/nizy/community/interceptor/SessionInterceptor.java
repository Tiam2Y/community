package top.nizy.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname SessionInterceptor
 * @Description TODO
 * @Date 2021/6/10 16:14
 * @Created by NZY271
 */
@Service //使Spring可以对其进行接管,才能自动注入 UserMapper
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private UserMapper userMapper;

    //需要实现三个方法

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //需要获取 Cookie 来判断用户是否是已登录的
        //使用 request 来获取 Cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByCookie(token);
                    if (user != null)
                        //说明数据库保留了该登陆过的用户
                        //在Session中写入该用户信息--在页面展示
                        request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
