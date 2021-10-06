package com.bambi.straw.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bambi.straw.portal.mapper")
public class StrawPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawPortalApplication.class, args);
    }

}
