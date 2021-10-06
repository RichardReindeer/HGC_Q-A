package com.bambi.straw.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller来标注控制器，
//控制器的放啊返回字符串就可以指定模板名称了；
@Controller
@Slf4j
public class HomeController {
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    public static final GrantedAuthority STUDENT =
            new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER =
            new SimpleGrantedAuthority("ROLE_TEACHER");
    //判断不同的身份跳转不同的首页
    @GetMapping("/index.html")
    public String index(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        logger.info("这是拿到的userDetails对象 {}",userDetails.getUsername());
        if(userDetails.getAuthorities().contains(TEACHER)){
            return "index_teacher";
        }
        else if(userDetails.getAuthorities().contains(STUDENT)){
            return "index";
        }
        return null;
    }


    @GetMapping("/register.html")
    public String goRegister(){
        log.debug("开始跳转页面");
        return "register";
    }

    @GetMapping("/login.html")
    public String goLogin(){
        return "login";
    }

    /**
     * 显示学生提问面板
     */
    @GetMapping("/question/create.html")
    public String create(){
        return "question/create";
    }


    //角色判断
    //根据身份跳转不同的问题详情页
    @GetMapping("/question/detail.html")
    public String detail(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        if(userDetails.getAuthorities().contains(TEACHER)){
            return "question/detail_teacher";
        }else if(userDetails.getAuthorities().contains(STUDENT)){
            return "question/detail";
        }
        return null;
    }

    @GetMapping("/search.html")
    public String search(){
        return "search";
    }
}
