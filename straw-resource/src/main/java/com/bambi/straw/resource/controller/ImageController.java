package com.bambi.straw.resource.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/v1/images")
@Slf4j
public class ImageController {

    @Value("${spring.resources.static-locations}")
    private File resourcePath;

    @Value("${straw.resource.host}")
    private String resourceHost;

    @PostMapping
    public R uploadImage(MultipartFile imageFile) throws IOException {
        //  按照日期创建文件夹
        //  path:2021/4/19
        String path= DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now());
        //  folder:E:/upload/2021/4/19
        File folder= new File(resourcePath,path);
        folder.mkdirs();
        log.debug("上传的目标文件夹:{}",folder.getAbsolutePath());

        //确定上传文件的文件名
        //a.b.c.jpg
        //0123456
        //获得文件的原始文件名
        String fileName=imageFile.getOriginalFilename();
        //获得后缀名  .jpg
        String ext=fileName
                .substring(fileName.lastIndexOf("."));
        //随机创建文件名
        //jakhsdfkjhaksjhdfkj.jpg
        String name= UUID.randomUUID().toString()+ext;
        log.debug("上传文件名为:{}",name);
        //创建要上传的文件对象,并执行上传
        File file=new File(folder,name);
        imageFile.transferTo(file);
        log.debug("文件完整路径:{}",file.getAbsolutePath());

        //返回静态资源服务器可以访问这个图片的路径
        //拼接访问的路径
        String url=resourceHost+"/"+path+"/"+name;
        log.debug("Url:{}",url);
        //记住r对象中的message就是我们要访问的路径
        return R.ok(url);
    }
}
