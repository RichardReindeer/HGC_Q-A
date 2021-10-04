package com.bambi.straw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//下面注解是启动动态路由的效果
//包含启动EurekaClient的含义
@EnableZuulProxy

//下面注解的意思是 通知SpringBoot开启使用Redis共享session的功能
@EnableRedisHttpSession
public class StrawGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawGatewayApplication.class, args);
    }

    //实现Ribbon的对象--->RestTemplate
    @Bean
    //注入的RestTemplate对象要求支持负载均衡!!!!!
    @LoadBalanced
    public RestTemplate restTemplate(){
        //这里实例化对象，会将其保存到Spring容器中
        //任何当前项目的代价需要时，可以使用@Autowired或@Resource来获得
        return new RestTemplate();
    }
}
