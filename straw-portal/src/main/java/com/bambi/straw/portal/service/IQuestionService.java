package com.bambi.straw.portal.service;

import com.bambi.straw.portal.model.Question;
import com.bambi.straw.portal.vo.QuestionVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
public interface IQuestionService extends IService<Question> {

    //查询当前登录用户的所有问题
    //登录用户的用户名由控制层获得
    //因为更换了PageInfo类型，所以需要指定查询的页数和每页有几个对象，所以需要传递两个Integer
    PageInfo<Question> getMyQuestions(String username,Integer pageNum , Integer pageSize);

    //学生发布问题新增question的方法
    void saveQuestion(QuestionVo questionVo,String username);

    //根据用户id查询该用户问题数的方法
    int countQuestionByUserId(Integer userId);

    //分页查询讲师问题列表的方法
    PageInfo<Question> getQuestionByTeacherName(String teacherName,Integer pageNum,Integer pageSize);

    //根据问题id，查询问题信息的方法
    Question getQuestionById(Integer id);

}
