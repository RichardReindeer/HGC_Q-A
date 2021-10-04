package com.bambi.straw.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class StrawGatewayApplicationTests {

    @Autowired
    RestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void getTemplate(){
        //先定义要调用的Rest接口的url
        //sys-service是eureka中注册的客户端名称
        // /v1/auth/demo 调用的Rest接口的路径
        String url = "http://sys-service/v1/auth/demo";
        //发送调用请求
        //第一个参数是上面定的url，
        //第二个参数是被调用的控制器方法的返回值 类型的反射
        //底层Ribbon也是接收到json，转化成参数2反射到的类型
        String str = restTemplate.getForObject(url,String.class);
        System.out.println("str = " + str);
    }
}
