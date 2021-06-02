package top.nizy.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Classname IndexController
 * @Date 2021/6/1 16:44
 * @Created by NZY271
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "Index";
    }
}
