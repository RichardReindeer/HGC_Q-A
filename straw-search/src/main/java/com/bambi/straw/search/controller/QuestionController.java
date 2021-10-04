package com.bambi.straw.search.controller;

import com.bambi.straw.commons.vo.R;
import com.bambi.straw.search.service.IQuestionService;
import com.bambi.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
//可重复
@RequestMapping("/v1/questions")
public class QuestionController {
    @Resource
    private  IQuestionService questionService;


    @PostMapping("")
    public R<PageInfo<QuestionVo>> search(String key , Integer pageNum,
        @AuthenticationPrincipal UserDetails userDetails){
        if(key==null){
            key="";
        }
        Integer pageSize = 8;
        PageInfo<QuestionVo> pageInfo = questionService.search(key, userDetails.getUsername(),pageNum,pageSize);
        return R.ok(pageInfo);
    }
}
