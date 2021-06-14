package top.nizy.community.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import top.nizy.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname CustomizeExceptionHandler
 * @Description 只能拦截 可以 handle 的异常
 * @Date 2021/6/14 21:17
 * @Created by NZY271
 */

/**
 * 处理不了的异常(比如 404 之类的)，可以在 ErrorController 中处理
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
        //@ResponseBody //会返回JSON
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model) {
//        HttpStatus status = getStatus(request);
        if(ex instanceof CustomizeException){
            //可以明确的异常，直接进行相应处理(显示)
            model.addAttribute("message", ex.getMessage());

        }else{
            //未知异常，进行统一显示
            model.addAttribute("message", "服务器冒烟了，请稍后再试~");  //写入错误信息
        }
        return new ModelAndView("error");
    }

//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
}
