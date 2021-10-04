package com.bambi.straw.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//当前项目会以Eureka客户端启动,并在客户端指定的位置寻找客户中心进行注册
@EnableEurekaClient
public class StrawResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawResourceApplication.class, args);
    }

}
