package com.bambi.straw.faq.controller;


import com.bambi.straw.commons.model.Question;
import com.bambi.straw.commons.model.Tag;
import com.bambi.straw.commons.vo.R;
import com.bambi.straw.faq.service.IQuestionService;
import com.bambi.straw.faq.service.ITagService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/v1/tags")
public class TagController {

    @Autowired
    private ITagService tagService;
    @Autowired
    private IQuestionService questionService;

    //@GetMapping("")表示当前方法的路径就是"/v1/tags"
    @GetMapping("")
    public R<List<Tag>> tags(){
        //调用业务逻辑层代码来获取所有标签
        List<Tag> tags = tagService.getTags();
        //利用R对象返回查询到的所有标签
        return R.ok(tags);
    }

    //获取所有标签
    @GetMapping("/list")
    public List<Tag> list(){
        return tagService.getTags();
    }


    @RequestMapping("/tagsQuestion")
    public R<PageInfo<Question>> TagQuestion(
            @AuthenticationPrincipal UserDetails userDetails,
            Integer tagNum,
            Integer pageNum
    ) {
        if (userDetails == null) {
            return R.invalidRequest("用户信息异常");
        }
        if (tagNum == -1) {
            tagNum = 1;
        }
        Integer pageSize = 8;
        PageInfo<Question> questionWithTag = questionService.getQuestionWithTag(userDetails.getUsername(), tagNum, pageNum, pageSize);
        return R.ok(questionWithTag);
    }
}
