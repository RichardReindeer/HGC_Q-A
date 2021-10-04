package com.bambi.straw.search;

import com.bambi.straw.search.repository.QuestionRepository;
import com.bambi.straw.search.service.IQuestionService;
import com.bambi.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class SyncTest {

    @Resource
    private IQuestionService iQuestionService;
    @Autowired
    private QuestionRepository questionRepository;


    //记住如果换索引要重新同步
    @Test
    void sync(){
        iQuestionService.sync();
    }

    @Test
    void find(){
        Page<QuestionVo> questionVos = questionRepository.queryAllByParams("java","java",11, PageRequest.of(0,2));
        questionVos.forEach(questionVo -> System.out.println("questionVo = " + questionVo));
    }

    @Test
    void service(){
        PageInfo<QuestionVo> pageInfo = iQuestionService.search("java","st2",2,3);
        pageInfo.getList().forEach(questionVo -> System.out.println("questionVo = " + questionVo));
    }
}
