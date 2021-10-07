package com.bambi.straw.faq.controller;


import com.bambi.straw.commons.model.Question;
import com.bambi.straw.commons.model.User;
import com.bambi.straw.commons.service.ServiceException;
import com.bambi.straw.commons.vo.R;
import com.bambi.straw.faq.service.IQuestionService;
import com.bambi.straw.faq.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.flogger.Flogger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/v1/questions")
@Slf4j
public class QuestionController {
    private static Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    IQuestionService questionService;

    //查询登录学生问题列表的方法
    @GetMapping("/my")
    public R<PageInfo<Question>> my(
            //想获取当前登录用户信息的写法
            //下面的注解会从Spring-Security中获得用户详情对象
            @AuthenticationPrincipal UserDetails userDetails,
            Integer pageNum
            ){
        log.debug("开始查询当前登录用户问题列表,用户名{}"+userDetails.getUsername());
        //定死查询条数
        Integer pageSize = 8;
        //调用查询所有学生问题列表
        PageInfo<Question> pageInfo = questionService.getMyQuestions(userDetails.getUsername(),pageNum,pageSize);
        return R.ok(pageInfo);
    }

    //新增问题控制器方法
    @PostMapping("")
    public R createQuestion(
            @Validated
                    QuestionVo questionVo, BindingResult result,
            @AuthenticationPrincipal
            UserDetails userDetails){
        log.debug("获得的实体类信息:{}",questionVo);

        //如果用户填写的信息有错误，则将错误信息记录
        if(result.hasErrors()){
            String message = result.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(message);
        }
         //这里应该调用 业务逻辑层来进行新增操作
        try{
            questionService.saveQuestion(questionVo, userDetails.getUsername());
            return R.ok("发布问题完成");
        }catch (ServiceException exception){
            log.error("保存失败",exception);
            return R.failed(exception);
        }

    }

    //讲师首页的控制层方法
    @GetMapping("/teacher")
    //下面注解表示要想运行控制器方法，必须拥有ROLE_TEACHER的角色
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public R<PageInfo<Question>> teacher(
            @AuthenticationPrincipal
            UserDetails userDetails,
            Integer pageNum
    ){
        if(pageNum==null) pageNum=1;
        Integer pageSize = 8;
        PageInfo<Question> pageInfo = questionService.getQuestionByTeacherName(userDetails.getUsername(),pageNum,pageSize);
        return R.ok(pageInfo);
    }

    //根据用户id查询用户对象的控制器方法
    //SpringMVC控制器允许使用{id}作为路径占位符，在有请求的时候会自动匹配并赋值给{id}的位置
    //例如 请求路径:/v1/question/151  <-----尽量匹配
    //那么151这个值就会自动赋值给id

    @GetMapping("/{id}")
    //@PathVariable表示匹配路径中的占位符，参数名称必须和占位符一致
    public R<Question> question(@PathVariable Integer id){
        if(id==null){
            //非法请求
            return R.invalidRequest("ID不能为空");
        }

        Question question = questionService.getQuestionById(id);
        return R.ok(question);
    }

    //根据用户id查询问题数的控制器方法
    //用于sys模块的Ribbon调用，是一个Rest接口
    @GetMapping("/count")
    public Integer count(Integer userId){
        return questionService.countQuestionByUserId(userId);
    }

    //分页查询所有问题的Rest接口
    @GetMapping("/page")
    public List<Question> questions(Integer pageNum,Integer pageSize){
        PageInfo<Question> questionPageInfo = questionService.getQuestion(pageNum,pageSize);
        return questionPageInfo.getList();
    }

    //查询当前所有问题按照指定页面大小的总页数
    @GetMapping("/page/count")
    public Integer pageCount(Integer pageSize ){
        //MybatisPlus提供的返回当前表总行数的方法
        int rows = questionService.count();
        //根据之前学习的分页知识计算总页数
        return (rows+pageSize-1)/pageSize;
    }

    @RequestMapping("/hotQuestion")
    public R<PageInfo<Question>> hotQuestions(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        logger.info("hotQuestions is starting");
        if(userDetails==null){
            logger.error("userDetails is null");
            R.invalidRequest("用户信息异常");
        }
        PageInfo<Question> hotQuestion = questionService.getHotQuestion(userDetails.getUsername());
        logger.info("hotQuestion list size {}",hotQuestion.getSize());
        return R.ok(hotQuestion);
    }
}
