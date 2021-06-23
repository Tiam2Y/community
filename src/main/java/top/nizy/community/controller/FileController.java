package top.nizy.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.nizy.community.dto.FileDTO;
import top.nizy.community.provider.OSSProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @Classname FileController
 * @Description 用于文件上传(只针对图片进行处理)
 * @Date 2021/6/22 16:12
 * @Created by NZY271
 */
@Controller
@Slf4j
public class FileController {
    @Autowired
    private OSSProvider oSSProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //editor.md 中引用的图片文件的对象
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            File newFile = new File(file.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(newFile);
            outputStream.write(file.getBytes());
            outputStream.close();
            file.transferTo(newFile);
            //返回图片的URL
            String fileUrl = oSSProvider.upLoadImage(newFile);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileUrl);
            return fileDTO;
        } catch (Exception e) {
            log.error("upload error", e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return fileDTO;
        }
    }
}

