package com.bambi.straw.faq.controller;


import com.bambi.straw.commons.model.Answer;
import com.bambi.straw.commons.vo.R;
import com.bambi.straw.faq.service.IAnswerService;
import com.bambi.straw.faq.vo.AnswerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("/v1/answers")
@Slf4j
public class AnswerController {

    @Resource
    private IAnswerService answerService;

    //编写新增讲师回复的控制器方法
    //postMapping后面什么都不写相当于双引号("")
    @PostMapping
    //只有老师能回答问题，所以限制有回答权限或者有老师身份的人才能运行这个方法
    //
    //@PreAuthorize("hasAuthority('/question/answer')")//限制
    //上面的方法也可以，但是也可以直接判断是否是老师这个《角色》
    @PreAuthorize("hasRole('TEACHER')")//自动帮你加ROLE进行权限判断
    public R<Answer> postAnswer(
            @Validated AnswerVo answerVo, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        log.debug("接收信息:{}",answerVo);
        if(result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            //错误信息，（验证结果)
            return R.unproecsableEntity(msg);
        }
        //调用业务逻辑层
        //200一般用于查询
        //201一般用于新增
        Answer answer = answerService.saveAnswer(answerVo,userDetails.getUsername());
        return R.created(answer);
    }

    //查询当前问题所有回答的控制器方法
    //这个控制器响应的路径url可能是
    //localhost:8080/v1/answers/question/151
    //意思是查询151号问题的所有回答

    @GetMapping("/question/{id}")
    public R<List<Answer>> questionAnswers(
            @PathVariable Integer id
    ){
        if(id==null) return R.invalidRequest("问题ID不能为空");
        List<Answer> answers = answerService.getAnswersByQuestion(id);
        return R.ok(answers);
    }

    @GetMapping("/{id}/solved")
    public R solved(
            @PathVariable Integer id
    ){
        boolean isAccept = answerService.accept(id);
        if(isAccept){
            return R.ok("采纳成功");
        }else {
            return R.notFound("采纳失败");
        }
    }

}
