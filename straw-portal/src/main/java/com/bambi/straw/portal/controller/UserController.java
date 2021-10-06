package com.bambi.straw.portal.controller;


import com.bambi.straw.portal.model.User;
import com.bambi.straw.portal.service.IUserService;
import com.bambi.straw.portal.vo.R;
import com.bambi.straw.portal.vo.UserVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
//@RestController 注解的类所有的方法自带@ResourceBody
/*为了后期修改为微服务项目减少工作量，一开始控制器路径设置为支持微服务版本的*/
@RequestMapping("/v1/users")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("/master")
    public R<List<User>> master(){
        List<User> masters = userService.getMasters();
        return R.ok(masters);
    }

    //获得当前用户信息
    @GetMapping("/my")
    public R<UserVo> my(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        UserVo userVo = userService.getCurrentUserVo(userDetails.getUsername());
        return R.ok(userVo);
    }
}
