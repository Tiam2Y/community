package top.nizy.community.dto;

import lombok.Data;
import org.springframework.web.servlet.ModelAndView;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.exception.CustomizeException;

/**
 * @Classname ResultDTO
 * @Description 用于向前端返回一些请求状态 -- 用户友好，可以及时根据错误修改 -- 与用户进行交互
 * (而不是异常处理的 / error 页面)
 * @Date 2021/6/15 16:42
 * @Created by NZY271
 */
@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO ok() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }
}
