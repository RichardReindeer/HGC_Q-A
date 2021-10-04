package com.bambi.straw.faq;

import com.bambi.straw.commons.model.Question;
import com.bambi.straw.faq.service.IQuestionService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class StrawFaqApplicationTests {

    @Resource
    IQuestionService questionService;
    @Test
    void contextLoads() {
        PageInfo<Question> questionList = questionService.getMyQuestions("st2",1,8);
        questionList.getList().forEach(question -> System.out.println("question = " + question));
    }

}
