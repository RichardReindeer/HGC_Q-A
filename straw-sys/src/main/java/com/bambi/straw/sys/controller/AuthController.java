package com.bambi.straw.sys.controller;

import com.bambi.straw.commons.model.Permission;
import com.bambi.straw.commons.model.Role;
import com.bambi.straw.commons.model.User;
import com.bambi.straw.sys.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {

    //这个方法设计为Ribbon的调用目标
    //即服务的提供者
    //这个控制器方法没有特殊的标志，想返回什么都可以(一般是不反悔R的)
    //这样的控制器方法有一个统一的称呼:----> Rest调用接口
    @GetMapping("/demo")
    public String demo(){
        return "hello Ribbon";
    }


    //供其他微服务访问的控制器方法
    @Autowired
    private IUserService userService;

    @GetMapping("/user")
    public User getUser(String username){
        User user = userService.getUserByUsername(username);
        return user;
    }

    @GetMapping("/permissions")
    public List<Permission> getPermissions(Integer userId){
        return userService.getUserPermission(userId);
    }

    @GetMapping("/roles")
    public List<Role> getRoles(Integer userId){
        return userService.getUserRoles(userId);
    }


}
