package com.bambi.straw.portal.service;

import com.bambi.straw.portal.model.Answer;
import com.bambi.straw.portal.vo.AnswerVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
public interface IAnswerService extends IService<Answer> {
    //添加回答的方法，有返回值
    //表单传递过来的answerVo对象
    Answer saveAnswer(AnswerVo answerVo,String username);

    //根据问题id查询这个问题的所有回答的方法
    List<Answer> getAnswersByQuestion(Integer questionId);

    //采纳回答
    boolean accept(Integer answerId);
}
