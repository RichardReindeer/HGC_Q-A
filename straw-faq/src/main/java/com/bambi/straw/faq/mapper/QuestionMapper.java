package com.bambi.straw.faq.mapper;


import com.bambi.straw.commons.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {
    @Select("select q.* from question q left join user_question uq on q.id = uq.question_id" +
            " where q.user_id = #{userId} or uq.user_id= #{userId}" +
            " order by q.createtime desc")
    List<Question> findTeacherQuestions(Integer userId);

    @Update("update question set status = #{status} where id = #{questionId}")
    Integer updateStatus(@Param("questionId") Integer questionId,
        @Param("status") Integer status
    );
}
