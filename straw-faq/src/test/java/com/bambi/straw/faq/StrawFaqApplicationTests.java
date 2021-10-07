package com.bambi.straw.faq;

import com.bambi.straw.commons.model.Question;
import com.bambi.straw.faq.service.IQuestionService;
import com.bambi.straw.faq.service.IUserCollectService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class StrawFaqApplicationTests {

    @Resource
    IQuestionService questionService;
    @Resource
    IUserCollectService userCollectService;

    @Test
    void contextLoads() {
        PageInfo<Question> questionList = questionService.getMyQuestions("st2", 1, 8);
        questionList.getList().forEach(question -> System.out.println("question = " + question));
    }

    @Test
    void hotQuestion() {
        PageInfo<Question> st2 = questionService.getHotQuestion("st2");
        st2.getList().forEach(question -> {
            System.out.println("Question = " + question.getTitle());
        });
    }

    @Test
    void collect() {
        Integer integer = userCollectService.countQuestionCollectionByUserId(11);
        System.out.println(integer);
    }

    @Test
    void setUserCollectService() {
        PageInfo<Question> st2 = questionService.getUsersCollectQuestion("st2",1,8);
        st2.getList().forEach(question -> {
            System.out.println(question.getTitle());
        });
    }

}
