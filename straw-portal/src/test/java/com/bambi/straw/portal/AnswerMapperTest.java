package com.bambi.straw.portal;

import com.bambi.straw.portal.mapper.AnswerMapper;
import com.bambi.straw.portal.model.Answer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class AnswerMapperTest {

    @Autowired
    private AnswerMapper answerMapper;

    @Test
    void setAnswerMapper(){
        List<Answer> answers  = answerMapper.findAnswersWithCommentByQuestionId(157);
        answers.forEach(answer -> System.out.println("answer = " + answer));
    }
}
