package com.bambi.straw.sys.vo;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 注册所用的值对象
 */
@Data
public class RegisterVo implements Serializable {

    //NotBlank要求这个字符串不能为null 或 "",也不能是空格  (先调用trim , 调用完毕之后判断是否长度是0)
    //如果出错则将message传递过去
    //如果出现问题所给予的反馈
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^1\\d{10}$",message = "学号格式不正确")
    private String phone;
    @NotBlank(message = "姓名不能为空")
    @Pattern(regexp = "^.{2,20}$",message = "昵称长度不能小于2位且不能大于20位")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{6,20}$",message = "密码长度6到20位")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String confirm;

}
