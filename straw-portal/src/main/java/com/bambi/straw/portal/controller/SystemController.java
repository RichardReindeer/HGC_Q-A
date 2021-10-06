package com.bambi.straw.portal.controller;

import com.bambi.straw.portal.service.IUserService;
import com.bambi.straw.portal.service.ServiceException;
import com.bambi.straw.portal.vo.R;
import com.bambi.straw.portal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 系统控制器
 */
@RestController
@Slf4j
public class SystemController {
    @GetMapping("/login.html")
    public ModelAndView loginForm(){
        //ModelAndView 返回"login" 本质上返回template/login.html这个模板
        //这是Spring的约定
        return new ModelAndView("login");
    }

    @GetMapping("/register.html")
    public ModelAndView registerForm(){
        return  new ModelAndView("register");
    }

    @Autowired
    private IUserService userService;

    //注册学生的控制器方法
    //因为页面使用了post的方式提交，所以使用PostMapping
    @PostMapping("/register")
    //因为执行注册的时候还没有登录，所以还需要执行放行列表
    public R registerStudent(
            //当一个实体类前加了@Validated注解
            //表示这个实体类的内容要被SpringValidation验证
            //验证完毕之后，会生成一个BindingResult的类型对象,这个对象中来保存着验证的结果信息
            @Validated RegisterVo registerVo, BindingResult bindingResult){ //因为是注册(增操作)所以不用指定什么泛型
        log.debug("获得的注册信息:{}",registerVo);
        //判断result结果中有没有错误
        if(bindingResult.hasErrors()){
            //如果验证结果有错误，
            //获得这个错误
            String error = bindingResult.getFieldError().getDefaultMessage();
            //利用R类返回错误信息给页面
            return R.unproecsableEntity(error);
        }

        if(!registerVo.getPassword().equals(registerVo.getConfirm())){
            return R.unproecsableEntity("两次密码输入不一致");
        }
        //这里要调用业务逻辑层代码，但是业务逻辑层代码可能发生异常
        //我们要根据是否发生异常来判断注册是否成功，并反馈出信息
        try {
            userService.registerStudent(registerVo);
            return R.created("注册完成");
        }catch (ServiceException e) {
            //error方法的第二个参数就是异常对象，会输出错误信息到控制台
            log.error("注册失败 " ,e);//error的日志写法不用写{}
            //使用R类，将错误信息返回给页面
            return R.failed(e);
        }

    }

    @PostMapping("/upload/file")
    /**
     * SpringMvc框架下
     * 使用MultipartFile来接收上传的图片
     * 参数的名字需要和form表单中的name属性相同***********************
     */
    public R uploadFile(MultipartFile imageFile) throws IOException {
        //1.确定并创建要保存用户上传文件的文件夹
        File folder = new File("C:/upload/");
        //创建指定文件夹，带S
        folder.mkdirs();
        //2.确定文件名
        //获得原始文件名，即用户上传文件时的文件名
        String fileName = imageFile.getOriginalFilename();
        //3.构建[文件名称]+[文件名] 格式的file对象
        //构建"C:/upload/"+fileName  这样的格式
        File file = new File(folder,fileName);
        log.debug("保存路径为:{}",file.getAbsolutePath());
        //4.执行上传
        imageFile.transferTo(file);
        log.debug("文件转换完成");
        //5.返回信息
        return R.ok("文件上传完成");
    }

    //获得保存文件的路径和访问路径
    @Value("${hgc.resource.path=file:C:/upload}")
    private File resourcePath;

    @Value("${hgc.resource.host=http://localhost:8899}")
    private String resourceHost;

    @PostMapping("/upload/image")
    public R uploadImage(MultipartFile imageFile) throws IOException {
        //按照日期创建文件夹
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now());
        File folder = new File(resourcePath,path);
        folder.mkdirs();
        log.debug("上传的目标文件夹:{}",folder.getAbsolutePath());

        //确定上传文件的文件名
        //获取后缀名
        String fileName = imageFile.getOriginalFilename();//获得原始文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));//获取文件名最后的一个.
        //随机生成UUID
        String name = UUID.randomUUID().toString()+suffix;
        log.debug("上传的文件名为:{}",name);
        //创建要上传的文件对象并执行上传
        File file = new File(folder,name);
        imageFile.transferTo(file);
        log.debug("文件的完成路径拼接完成:{}",file.getAbsolutePath());

        //返回静态资源服务器可以访问这个图片的路径
        //拼接访问路径
        String url = resourceHost+"/"+path+"/"+name;
        log.debug("url:{}"+url);
        //记住R对象中的message 就是要访问的路径
        return R.ok(url);
    }
}
