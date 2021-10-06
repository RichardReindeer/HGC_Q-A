package com.bambi.straw.portal;

import com.bambi.straw.portal.mapper.QuestionMapper;
import com.bambi.straw.portal.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class QuestionMapperTest {
    @Autowired
    QuestionMapper questionMapper;
    @Test
    void teacher(){
        List<Question> questions = questionMapper.findTeacherQuestions(3);
        questions.forEach(question -> System.out.println("question = " + question));
    }
}
