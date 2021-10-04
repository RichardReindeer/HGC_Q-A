package com.bambi.straw.sys.mapper;


import com.bambi.straw.commons.model.Permission;
import com.bambi.straw.commons.model.Role;
import com.bambi.straw.commons.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bambi.straw.sys.vo.UserVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author MR.Bambi
* @since 2021-04-14
*/
    @Repository
    public interface UserMapper extends BaseMapper<User> {

        //根据用户名查询用户对象
        @Select("select * from user where username = #{username}")
        User findUserByUserName(String userName);

        //根据用户id，查询所有权限(返回的是权限的集合)
        @Select("SELECT " +
                " p.id , p.name " +
                " from " +
                " user u left join user_role ur on u.id = ur.user_id " +
                " left join role r on r.id = ur.role_id " +
                " left join role_permission rp on r.id = rp.role_id " +
                " left join permission p on p.id = rp.permission_id " +
                " where u.id = #{id}")
        List<Permission> findUserPermissionsById(Integer id);

        //根据当前登录的用户名查询UserVo类型的信息
        @Select("select id , username , nickname from user where username = #{username}")
        UserVo findUserVoByUserName(String username);

        /*
            查询用户角色
         */
        @Select("select r.id,r.name" +
                " from user u" +
                " left join user_role ur on u.id=ur.user_id" +
                " left join role r       on r.id=ur.role_id" +
                " where u.id=#{id}")
        List<Role> findUserRolesById(Integer id);

    }
