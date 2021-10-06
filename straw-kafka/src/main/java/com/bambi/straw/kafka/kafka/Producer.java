package com.bambi.straw.kafka.kafka;

import com.bambi.straw.kafka.model.Message;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 这个类是生产者类
 * 是用来向kafka发送信息的
 *
 * 每隔8秒运行一次发送信息的方法
 * 需要将这个类注入到spring容器中
 */
@Component
@Slf4j

public class Producer implements Serializable {

    //使用注解，注入 Spring提供的操作kafka的类
    @Autowired
    //<String,String> 第一个指定要发送的话题名称 第二个指定的是要发送信息的对象的json字符串
    private KafkaTemplate<String,String> kafkaTemplate ;

    int i =1;

    //定时运行 每隔8秒运行一次方法
    //可以设置在指定的月份中的那一天执行
    //fixedRate 后接毫秒数
    @Scheduled(fixedRate = 8*1000)
    public void sendMessage(){
        //向kafka发送一个message对象的信息
        Message message = new Message()
                .setId(i++)
                .setMessage("我是生产者，开始生产了")
                .setTime(System.currentTimeMillis());
        //将java对象转为json格式 可以相互转换
        Gson gson = new Gson();
        String json = gson.toJson(message);
        log.debug("发送信息:{}",json);
        //使用kafkaTemplate发送信息
        kafkaTemplate.send("myTopic",json);
    }

}
