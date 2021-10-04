package com.bambi.straw.sys.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/demo")

//测试能否共享session
public class ControllerDemo {

    @GetMapping("/test")
    public String test(){
        return "测试一下";
    }

    //测试共享session效果的控制器方法
    @GetMapping("/testRedis")
    public String redis(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        System.out.println("当前登录用户名:"+userDetails.getUsername());
        return userDetails.getUsername()+"这是一个用户名"+"    "+userDetails;
    }
}
