package com.bambi.straw.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CommentVo implements Serializable {
    //需要经过Spring Validation 的验证
    /**
     * 对Integer  notNull
     * 对String   notBlank
     *
     */
    @NotNull(message = "回答id不能为空")
    private Integer answerId;
    @NotBlank(message = "回答内容不能为空")
    private String content;

}
