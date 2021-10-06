package com.bambi.straw.portal.security;

import com.bambi.straw.portal.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

//表示当前的类是Spring的配置类
@Configuration
//这个配置类是配置Spring-Security的
//perPostEnabled = true <----表示启动权限管理功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
//继承父类 WSCA
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //为了方便实现框架中规定的方法达到设置权限的目的，
    //继承WebSecurityConfigurerAdapter类重写其中的方法

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    //重写Configure方法参数为:AuthenticationManagerBuilder
    //这个方法是规定用户登陆验证信息的
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置类中规定当用户登录时调用下面的方法来进行登录验证
        //这个方法会调用userDetailsService对象的loadUserByUsername 方法
        //并会自动将接收到的用户名传给方法的参数
        //方法返回用户详情(UserDetails)对象后，SpringSecurity内部会自动 验证登录结果
        auth.userDetailsService(userDetailsService);
    }

    //day2 下午；设置Spring security放行的网页

    //这个方法是Spring-Security框架设置授权范围路径的方法
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       //使用http参数设置各种授权功能
        http.csrf().disable()
        //关闭防跨域攻击的功能；不关闭容易出现异常  <---因为未来的微服务使用了跨域，可能会被springSecurity识别为跨域攻击给拦截，所以关闭
        .authorizeRequests() //设置所有请求的授权
        .antMatchers(
                //配置下列路径的授权 可变参数  传入可放行的页面(不仅要设置放行的页面，也要设置放行的资源)
                "/js/*",
                "/css/*",
                "/img/**",
                "/bower_components/**",
                "/login.html",
                "/register.html",
                "/register"
        ).permitAll() //设置上述所有路径不需要登录就可以访问(放行)
        .anyRequest() //其他的所有请求
        .authenticated() //需要进行登录验证
        .and()        //上面的配置完毕，开始另一个配置
        .formLogin() //使用表单登录
        .loginPage("/login.html") //设置登录页面为<---
        .loginProcessingUrl("/login") //设置处理登录的路径，SpringSecurity默认的
        .failureUrl("/login.html?error") //设置登录失败访问的页面
        .defaultSuccessUrl("/index.html") //登陆成功访问的页面
        .and()                            //以上配置完毕后，开始另一个配置
        .logout()                         //开始设置登出信息
        .logoutUrl("/logout")             //登出路径
        .logoutSuccessUrl("/login.html?logout");  //设置登出后显示的页面

    }
}
