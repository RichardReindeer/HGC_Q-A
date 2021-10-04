package com.bambi.straw.faq.kafka;

import com.bambi.straw.commons.model.Question;
import com.bambi.straw.commons.vo.Topic;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Slf4j
public class KafkaProducer implements Serializable {
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;
    //将学生提问的问题发送到Kafka中
    public void sendQuestion(Question question){
        //转换成json
        Gson gson = new Gson();
        String json = gson.toJson(question);
        log.debug("发送信息到kafka:{}",json);
        //开始发送信息
        kafkaTemplate.send(Topic.QUESTIONS,json);
    }
}
