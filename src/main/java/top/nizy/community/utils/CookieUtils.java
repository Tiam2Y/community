package top.nizy.community.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

/**
 * @Classname CookieUtils
 * @Description TODO
 * @Date 2021/7/19 14:29
 * @Created by NZY271
 */
@Component
public class CookieUtils {
    public Cookie getCookie(String key, String value, int time) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(time);
        cookie.setPath("/");
        return cookie;
    }
}
