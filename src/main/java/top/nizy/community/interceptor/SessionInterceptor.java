package top.nizy.community.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.nizy.community.dto.ResultDTO;
import top.nizy.community.dto.UserDTO;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.model.UserExample;
import top.nizy.community.service.NotificationService;
import top.nizy.community.utils.TokenUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

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

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TokenUtils tokenUtils;

    @Value("${gitee.redirect.uri}")
    private String giteeRedirectUri;
    @Value("${github.redirect.uri}")
    private String githubRedirectUri;


    //需要实现三个方法

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置 context 级别的属性
        request.getServletContext().setAttribute("giteeRedirectUri", giteeRedirectUri);
        request.getServletContext().setAttribute("githubRedirectUri", githubRedirectUri);
        //需要获取 Cookie 来判断用户是否是已登录的
        //使用 request 来获取 Cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token") && StringUtils.isNotBlank(cookie.getValue())) {
                    String token = cookie.getValue();
                    if(token != null){
                        ResultDTO<Object> resultDTO = tokenUtils.verifyToken(token);
                        if (resultDTO.getCode() == 200) {
                            UserDTO userDTO = (UserDTO) resultDTO.getData();
                            HttpSession session = request.getSession();
                            session.setAttribute("user", userDTO);
                            Long unreadCount = notificationService.unreadCount(userDTO.getId());
                            session.setAttribute("unreadCount", unreadCount);
                        }
                    }
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
