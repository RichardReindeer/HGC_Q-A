package com.bambi.straw.kafka.kafka;

import com.bambi.straw.kafka.model.Message;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Slf4j
@Component
//Spring启动时，将当前类注入到Spring容器，才能接收到同属于Spring容器中producer发送的信息
public class Consumer implements Serializable {

    //监听器注解
    //指定要监听的话题名
    //这个监听器时刻关注myTopic话题中的消息
    //如果没有消息，进入待机状态
    //如果出现消息，立即将消息获取，并调用下面的方法将消息赋值给方法的参数
    //如果消息比较多，已经开始排队，会不断运行，直到把队列中的消息处理完为止
    @KafkaListener(topics = "myTopic")
    //参数必须为ConsumerRecord
    public void receivedMessage(ConsumerRecord<String,String> record){
        //获取消息中的字符串信息(json)
        //通过.value获取json对象
        String json = record.value();
        Gson gson = new Gson();
        Message message = gson.fromJson(json,Message.class);
        log.debug("获得的message对象:{}",message);

    }

}
