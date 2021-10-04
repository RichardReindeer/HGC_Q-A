package com.bambi.straw.faq.service.impl;


import com.bambi.straw.commons.model.Answer;
import com.bambi.straw.commons.model.Question;
import com.bambi.straw.commons.model.User;
import com.bambi.straw.commons.service.ServiceException;
import com.bambi.straw.faq.mapper.AnswerMapper;
import com.bambi.straw.faq.mapper.QuestionMapper;
import com.bambi.straw.faq.ribbon.RibbonClient;
import com.bambi.straw.faq.service.IAnswerService;
import com.bambi.straw.faq.vo.AnswerVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {
    @Autowired
    private AnswerMapper answerMapper;
    //需要根据用户名获取用户信息，所以需要调用
    @Resource
    private RibbonClient ribbonClient;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    @Transactional
    public Answer saveAnswer(AnswerVo answerVo, String username) {
        //1.获得用户信息
        User user = ribbonClient.getUser(username);
        //2.构建Answer对象
        Answer answer = new Answer()
                .setContent(answerVo.getContent())
                .setQuestId(answerVo.getQuestionId())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setCreatetime(LocalDateTime.now())
                .setLikeCount(0)
                .setAcceptStatus(0);
        //3.新增
        int num = answerMapper.insert(answer);
        if (num != 1) throw ServiceException.busy();
        //4.返回
        return answer;
    }

    //根据问题id查询出回答表中当前问题的所有回答
    @Override
    public List<Answer> getAnswersByQuestion(Integer questionId) {
        /*//根据id查询出回答表中当前问题所有回答
        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("quest_id",questionId);
        List<Answer> answers = answerMapper.selectList(queryWrapper);*/
        //将现在代码重构为包含评论回答的版本
        List<Answer> answers = answerMapper.findAnswersWithCommentByQuestionId(questionId);

        return answers;
    }

    //采纳回答
    @Override
    @Transactional
    public boolean accept(Integer answerId) {

        Answer answer = answerMapper.selectById(answerId);
        Question question = questionMapper.selectById(answer.getQuestId());

        //判断当前问题是否已经处于已解决状态
        if(question.getStatus()== Question.SOLVED){
            throw ServiceException.invalidRequest("问题已被解决");
        }

        //查询当前回答，判断是否已经被采纳
        if(answer.getAcceptStatus()!=null && answer.getAcceptStatus()==1){
            throw ServiceException.notFound("该问题已经被采纳");
        }

        //更新answer的状态
        int num = answerMapper.updateAcceptStatus(answerId,1);
        if(num!=1){
            throw ServiceException.busy();
        }
        //更新question状态
        num = questionMapper.updateStatus(question.getId(),Question.SOLVED);
        return true;
    }
}
