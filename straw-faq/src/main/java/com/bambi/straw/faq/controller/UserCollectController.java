package com.bambi.straw.faq.controller;


import com.bambi.straw.faq.service.IUserCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/v1/userCollects")
public class UserCollectController {
    @Autowired
    private IUserCollectService userCollectService;
    //根据用户id查询收藏数的控制器方法
    //用于sys模块的Ribbon调用，是一个Rest接口
    @GetMapping("/count")
    public Integer count(Integer userId){
        return userCollectService.countQuestionCollectionByUserId(userId);
    }
}
