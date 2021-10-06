package com.bambi.straw.faq.controller;

import com.bambi.straw.commons.model.Answer;
import com.bambi.straw.commons.vo.R;
import com.bambi.straw.faq.service.IAnswerService;
import com.bambi.straw.faq.vo.AnswerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 * 答案控制器层
 * 对老AnswerController进行代码可读性优化
 * <pre>
 * HISTORY
 * ****************************************************************************
 *  ID     DATE          PERSON          REASON
 *  1      2021/10/6 20:44    Bambi        Create
 * ****************************************************************************
 * </pre>
 *
 * @author Bambi
 * @since 1.0
 */
@RestController
@RequestMapping("/v1/answers")
public class AnswerController {
    private static Logger logger = LoggerFactory.getLogger(AnswerController.class);

    @Resource
    private IAnswerService answerService;

    /**
     * 老师回复请求接收
     * 使用PreAuthorize验证老师身份
     *
     * @param answerVo    接收到的回复
     * @param result
     * @param userDetails 用户身份详情信息
     * @return 标准返回参数实体类 使用201返回新增操作，使用200返回查询操作
     */
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public R<Answer> postAnswer(
            @Validated AnswerVo answerVo, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("postAnswer is starting!!!");
        if (result.hasErrors()) {
            logger.info("has some Error in AnswerController postAnswer()");
            String defaultMessage = result.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(defaultMessage);
        }
        Answer answer = answerService.saveAnswer(answerVo, userDetails.getUsername());
        logger.info("answer is {}", answer.getContent());
        return R.created(answer);
    }

    /**
     * 根据问题id获取对应答案
     *
     * @param id 问题id
     * @return 该问题下的所有回复内容
     */
    @RequestMapping("/question/{id}")
    public R<List<Answer>> questionsAns(
            @PathVariable Integer id
    ) {
        logger.info("questionsAns is starting !!!!");
        if (id == null) {
            logger.error("question id is Null");
            return R.invalidRequest("问题ID不能为空");
        }
        List<Answer> answersByQuestion = answerService.getAnswersByQuestion(id);
        logger.info("questionsAns is ending !!");
        return R.ok(answersByQuestion);
    }


    /**
     * 根据答案id进行答案的采纳
     *
     * @param id
     * @return
     */
    @RequestMapping("/{id}/solved")
    public R solved(
            @PathVariable Integer id
    ) {
        logger.info("solved is starting ! ! !");
        if (id == null) {
            logger.error("id is null! ! ! ");
            return R.invalidRequest("id is null");
        }
        boolean accept = answerService.accept(id);
        if (accept) {
            logger.info("采纳成功");
            return R.ok("采纳成功");
        } else {
            logger.error("采纳失败，检查id是否正确");
            return R.notFound("采纳失败");
        }
    }
}
