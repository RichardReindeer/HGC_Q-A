package com.bambi.straw.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class HomeController {

    //为了方便角色的判断，我们定义了两个角色的常量
    //这种常量的类型是Spring-Security角色类型固定的
    //role写错是没有提示的!!!!!!!!!!
    public static final GrantedAuthority STUDENT= new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER= new SimpleGrantedAuthority("ROLE_TEACHER");

    //用于显示学生首页的控制器方法
    //用于显示页面的都是getMapping
    @GetMapping("/index.html")
    public ModelAndView index(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        //判断当前登录用户的角色
        //获取当前用户的所有权限
        if(userDetails.getAuthorities().contains(TEACHER)){
            return new ModelAndView("index_teacher");
        }else if(userDetails.getAuthorities().contains(STUDENT)){
            return new ModelAndView("index");
        }
        //既不是学生也不是老师
        return null;
    }

    /**
     * 显示user发布问题的控制器方法
     */
    @GetMapping("/question/create.html")
    public ModelAndView createQuestion(){
        return new ModelAndView("question/create");
    }

    @GetMapping("/question/detail.html")
    public ModelAndView detail(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        if(userDetails.getAuthorities().contains(TEACHER)){
            return new ModelAndView("question/detail_teacher");
        }else if(userDetails.getAuthorities().contains(STUDENT)){
            return new ModelAndView("question/detail");
        }
        //既不是老师也不是学生 return null
        return null;
    }
}
