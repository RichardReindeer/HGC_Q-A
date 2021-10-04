package com.bambi.straw.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


/**
 * Swagger配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    /**
     * 根据环境判断是否启用Swagger
     * @return
     */
    @Bean
    public Docket getDocket(Environment environment){

        Profiles profiles = Profiles.of("dev");
        //直接传入可变参数的方法过时了，可以传入一个profiles 并在其静态方法of()中进行可变参数的赋值,
        //可变参数为其指定的环境
        //boolean b = environment.acceptsProfiles(profiles);
        boolean b = true;
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .groupName("Bambi")
                .enable(b)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bambi.springboot.swagger.demo02.controller"))
                .build();
    }

    private ApiInfo getApiInfo(){
        Contact contact = new Contact("","","");

        ApiInfo apiInfo = new ApiInfo("Api Documentation",
                "Api Documentation",
                "1.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
        return apiInfo;
    }
}
