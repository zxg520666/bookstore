package com.zxg.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class UploadController {
    @Autowired
    private Environment env;
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return "上传失败，请选择文件";
        }
        String filePath=env.getProperty("file.upload.book");
        File dest = new File(filePath);
        if(!dest.exists()){
            dest.mkdirs();
        }
        //获取文件的原始名称
        String originalFilename = file.getOriginalFilename();
        //获取最后一个.的位置
        int lastIndexOf = originalFilename.lastIndexOf(".");
        //获取文件的后缀名 .jpg
        String suffix = originalFilename.substring(lastIndexOf);
        String filename=String.valueOf(System.currentTimeMillis()).concat(".").concat(suffix);
        File uploadImage = new File(dest, filename);
        try {
            file.transferTo(uploadImage);
            LOGGER.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.toString(), e);
        }
        return "上传失败！";
    }
}
