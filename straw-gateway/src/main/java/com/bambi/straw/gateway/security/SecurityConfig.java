package com.bambi.straw.gateway.security;

import com.bambi.straw.gateway.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//形成最基本的Security格式
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    //Http的方法

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(
                        "/img/**",
                        "/js/*",
                        "/css/*",
                        "/bower_components/**",
                        "/login.html",
                        "/register.html",
                        "/register",
                        //需要设置注册功能的放行，发送到sys模块
                        "/sys/v1/users/register"
                ).permitAll().anyRequest().authenticated()
                //登录和登出
                .and().formLogin()
                .loginPage("/login.html")
                //登录要到那个控制器
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index.html")
                .failureUrl("/login.html?error")
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout");


    }
}
