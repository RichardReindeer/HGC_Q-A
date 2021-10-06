package com.bambi.straw.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    //这个类是Spring-Security规定的实现类
    //调用loadUserByUserName这个方法才能获取到用户输入的用户名

    //这个方法需要调用编写好的业务逻辑层方法
    @Autowired
    private IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserDetails(username);
    }
}
