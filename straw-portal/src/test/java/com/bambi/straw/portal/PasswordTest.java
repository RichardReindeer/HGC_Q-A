package com.bambi.straw.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class PasswordTest {

    //加密代码
    @Test
    void enCode(){
        //编码加密
        /**
         * Spring-Security 提供的加密对象
         *
         */
        //下面的对象是按照BCrypt加密规则加密的对象
        //常见加密规则 -->MD5
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //将指定字符串(密码) 加密为密文密码字符串的方法如下
        //$2a$10$ddEeFwouNeTCz6K6XIfdieC2.qV6eg9eS0VXjVeRMIcEYIIiP7.TG
        //每次加密的结果都是不同的，这种现象就是随机加盐技术的结果
        String password =passwordEncoder.encode("888888");
        System.out.println("password = " + password);
    }

    //验证代码
    @Test
    void match(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //下面的方法是验证一个字符串是否匹配一个加密结果的方法
        //参数1是明文密码， 参数2是加密后的密码
        //返回值是明文密码是否可能生成参数2的结果
        Boolean b = passwordEncoder.matches("888888","$2a$10$ddEeFwouNeTCz6K6XIfdieC2.qV6eg9eS0VXjVeRMIcEYIIiP7.TG");
        System.out.println("b = " + b);
    }
}
