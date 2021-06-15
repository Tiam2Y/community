package top.nizy.community.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.nizy.community.dto.ResultDTO;
import top.nizy.community.exception.CustomizeErrorCode;
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
    //使得可以返回JSON
    @ResponseBody
    Object handle(HttpServletRequest request, Throwable ex, Model model) {

        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            //返回 JSON 而不是异常页面 -- 可以供用户及时修改 -- 可以进行交互
            if (ex instanceof CustomizeException) {
                return ResultDTO.errorOf((CustomizeException) ex);
            } else {
                //未知异常，进行统一显示
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
        } else {
            // 跳转至错误页面
            if (ex instanceof CustomizeException) {
                //可以明确的异常，直接进行相应处理(显示)
                model.addAttribute("message", ex.getMessage());
            } else {
                //未知异常，进行统一显示
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());  //写入错误信息
            }
            return new ModelAndView("error");
        }
    }
}
