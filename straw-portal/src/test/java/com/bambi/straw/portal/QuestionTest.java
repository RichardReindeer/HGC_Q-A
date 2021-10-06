package com.bambi.straw.portal;

import com.bambi.straw.portal.mapper.UserMapper;
import com.bambi.straw.portal.model.Question;
import com.bambi.straw.portal.service.IQuestionService;
import com.bambi.straw.portal.vo.QuestionVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class QuestionTest {
    @Autowired
    IQuestionService iQuestionService;
    @Autowired
    UserMapper userMapper;
    @Test
    void selectQ(){
        /*List<Question> questions = iQuestionService.getMyQuestions("st2");
        questions.forEach(question -> System.out.println("question = " + question));*/
    }

    @Test
    void add(){
        QuestionVo questionVo = new QuestionVo()
                .setTitle("什么是快乐星球")
                .setContent("如果你想知道什么是快乐星球，那我就带你研究")
                .setTagNames(new String[]{"Web","Ajax"})
                .setTeacherNicknames(new String[]{"王克晶"});
        iQuestionService.saveQuestion(questionVo,"st2");
    }
}
