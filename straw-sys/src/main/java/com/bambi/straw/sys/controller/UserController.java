package com.bambi.straw.sys.controller;


import com.bambi.straw.commons.model.User;
import com.bambi.straw.commons.service.ServiceException;
import com.bambi.straw.commons.vo.R;
import com.bambi.straw.sys.service.IUserService;
import com.bambi.straw.sys.vo.RegisterVo;
import com.bambi.straw.sys.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@RestController
@Slf4j
//@RestController 注解的类所有的方法自带@ResourceBody
/*为了后期修改为微服务项目减少工作量，一开始控制器路径设置为支持微服务版本的*/
@RequestMapping("/v1/users")
public class UserController {

    @Resource
    private IUserService userService;

    //这是查询所有老师的Rest接口
    //用于被Ribbon调用
    @GetMapping("/master")
    public List<User> master(){
        List<User> masters = userService.getMasters();
        return masters;
    }

    //这是查询所有老师的方法
    //用于被ajax调用
    @GetMapping("/masters")
    public R<List<User>> masters(){
        List<User> masters = userService.getMasters();
        return R.ok(masters);
    }
    //获得当前用户信息
    @GetMapping("/my")
    public R<UserVo> my(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        UserVo userVo = userService.getCurrentUserVo(userDetails.getUsername());
        return R.ok(userVo);
    }

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
}
