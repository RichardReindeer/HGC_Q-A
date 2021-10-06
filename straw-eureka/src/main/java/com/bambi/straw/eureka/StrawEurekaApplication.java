package com.bambi.straw.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class StrawEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawEurekaApplication.class, args);
    }

}
