package com.bambi.straw.faq.mapper;


import com.bambi.straw.commons.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.core.parameters.P;
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

    /**
     * 根据标签搜索对应问题
     * @param tagNum 标签id
     * @param userId 用户id
     * @return  符合条件的问题列表
     */
    @Select(
            "select title, id\n" +
                    "from question\n" +
                    "where id in (select question_id from question_tag where tag_id = #{tagNum}) and user_id = #{userId} and delete_status = 0;"
    )
    List<Question> findQuestionWithTag(@Param("tagNum") Integer tagNum,@Param("userId") Integer userId);

    /**
     * 获取问题，并按浏览量倒叙
     * @param userId
     * @return
     */
    @Select("select * from question where user_id = #{userId} and delete_status = 0 order by page_views desc ")
    List<Question> findHotQuestion(@Param("userId") Integer userId);
}
