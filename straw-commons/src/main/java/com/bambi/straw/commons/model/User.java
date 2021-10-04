package com.bambi.straw.commons.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 电话号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 所属班级id
     */
    @TableField("classroom_id")
    private Integer classroomId;

    /**
     * 注册时间
     */
    @TableField("createtime")
    private LocalDateTime createtime;

    /**
     * 账号是否可用，0-》否，1-》是
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 账号是否被锁住，0-》否，1-》是
     */
    @TableField("locked")
    private Integer locked;

    /**
     * 0-》学生，1-》回答问题的老师
     */
    @TableField("type")
    private Integer type;

    /**
     * 自我介绍
     */
    @TableField("self_introduction")
    private String selfIntroduction;


}
