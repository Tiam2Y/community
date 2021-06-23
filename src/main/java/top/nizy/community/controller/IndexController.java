package top.nizy.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.nizy.community.cache.HotTagCache;
import top.nizy.community.dto.HotTagDTO;
import top.nizy.community.dto.PaginationDTO;
import top.nizy.community.dto.QuestionDTO;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.User;
import top.nizy.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Autowired
    private QuestionService questionService;

    //用来读取隔一段时间更新的缓存的热门标签
    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(Model model,
                        //为了实现分页展示，需要从页面上获取展示的第page页，每页展示size项
                        //来决定PaginationDTO中参数的设置
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false, defaultValue = "") String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        //需要获取 Cookie 来判断用户是否是已登录的
        //直接利用的 SessionInterceptor 中的 preHandler

        //根据page和size确定数据库分页查询limit的参数 offset 和
        //返回PaginationDTO对象(封装了查询到的每页的内容)
        PaginationDTO<QuestionDTO> pagination = questionService.list(search, tag, sort, page, size);
        List<HotTagDTO> hotTags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("hotTags", hotTags);
        model.addAttribute("sort", sort);
        return "index";
    }
}
