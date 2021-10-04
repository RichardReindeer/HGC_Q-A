package com.bambi.straw.search.kafka;

import com.bambi.straw.commons.vo.Topic;
import com.bambi.straw.search.service.IQuestionService;
import com.bambi.straw.search.vo.QuestionVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Slf4j
public class KafkaConsumer implements Serializable {
    //要调用业务逻辑层方法
    @Resource
    private IQuestionService questionService;
    //定义监听器
    @KafkaListener(topics = Topic.QUESTIONS)
    public void receiveQuestion(ConsumerRecord<String,String> record){
        //获取kafka中的消息(json)
        String json  = record.value();
        log.debug("测试一下是不是获取到了:{}",json);
        Gson gson = new Gson();
        QuestionVo questionVo = gson.fromJson(json,QuestionVo.class);
        //执行业务逻辑层的新增方法
        questionService.saveQuestion(questionVo);
    }
}
