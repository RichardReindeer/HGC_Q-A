package com.bambi.straw.sys.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //重写放行方法

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭防跨域攻击
        //任何权限都放行 --->anyRequest().permitAll()
        http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
}
