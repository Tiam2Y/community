package top.nizy.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Classname IndexController
 * @Date 2021/6/1 16:44
 * @Created by NZY271
 */

/**
 * "@Controller"注解 -- 将当前类作为路由API的承载者
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
