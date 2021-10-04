package com.bambi.straw.faq.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
//支持实体类的链式函数方法
@Accessors(chain = true)
public class QuestionVo implements Serializable {

    @NotBlank(message = "标题不能为空")
    @Pattern(regexp = "^.{3,50}$",message = "标题要求3~50个字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    //为了不是空且遍历不报错
    @NotEmpty
    private String[] tagNames= {};
    @NotEmpty
    private String[] teacherNicknames = {};

}
