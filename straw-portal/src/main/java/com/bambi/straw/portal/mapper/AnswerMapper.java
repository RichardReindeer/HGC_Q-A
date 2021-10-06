package com.bambi.straw.portal.mapper;

import com.bambi.straw.portal.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author MR.Bambi
* @since 2021-04-14
*/
    @Repository
    public interface AnswerMapper extends BaseMapper<Answer> {

        //根据问题Id查询所有回答，这个回答中包含其所有的评论
        List<Answer> findAnswersWithCommentByQuestionId(Integer questionId);

        //修改当前回答的accept_status采纳状态
        @Update("update answer set accept_status = #{acceptStatus}" +
                " where id = #{answerId}")
        Integer updateAcceptStatus(@Param("answerId") Integer answerId , @Param("acceptStatus") Integer acceptStatus);

    }
