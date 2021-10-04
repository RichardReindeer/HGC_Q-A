package com.bambi.straw.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
//实现链式函数书写
@Accessors(chain = true)
public class UserVo implements Serializable {
    private Integer id;
    private String username;
    private String nickname;

    //问题数:默认值为0
    private int questions;
    //用户收藏数
    private int collections;

}
