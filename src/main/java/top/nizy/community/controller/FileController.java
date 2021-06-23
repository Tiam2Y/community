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
        File newFile = null;
        try {
            //阿里云的需要将 MultipartFile 类型转换为 File 类型
            newFile = multipartFileToFile(file);
            //返回图片的URL
            String fileUrl = oSSProvider.upLoadImage(newFile);
            //删除临时文件
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileUrl);
            return fileDTO;
        } catch (Exception e) {
            log.error("upload error", e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("图片上传失败");
            return fileDTO;
        } finally {
            deleteTempFile(newFile);
        }
    }

    /**
     * MultipartFile 转 File
     */
    public File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     */
    public void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            boolean delete = del.delete();
        }
    }
}

