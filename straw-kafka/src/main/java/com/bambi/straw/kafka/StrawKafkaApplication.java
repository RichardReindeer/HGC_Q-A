package com.bambi.straw.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
/*启动kafka*/
@EnableKafka
//这个为了测试才加的，为了周期性发送请求
//开启定时计划的功能
@EnableScheduling
public class StrawKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawKafkaApplication.class, args);
    }

}
