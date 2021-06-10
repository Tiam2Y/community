package top.nizy.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Classname WebConfig
 * @Description TODO
 * @Date 2021/6/10 16:11
 * @Created by NZY271
 */
@Configuration
//@EnableWebMvc   //会使得样式没有了？默认不对静态资源放行
public class WebConfig implements WebMvcConfigurer {

    @Autowired  //此处也需要注入
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //.addPathPatterns("/**")添加需要经过 Interception 处理的路径
        //.excludePathPatterns("/admin/**")排除哪些路径
        //registry.addInterceptor(new ThemeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");

    }
}