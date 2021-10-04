package com.bambi.straw.sys.service;


import com.bambi.straw.commons.model.Permission;
import com.bambi.straw.commons.model.Role;
import com.bambi.straw.commons.model.User;
import com.bambi.straw.sys.vo.RegisterVo;
import com.bambi.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
public interface IUserService extends IService<User> {

    //编写一个方法，来实现返回用户信息给SpringSecurity
    //这个方法的参数是Spring-Security提供的用户输入的用户名
    //返回值是Spring-Security 提供的一个保存用户详细信息的类型UserDetails
    //这个方法的目的就是连接数据库，收集用户的详细信息并返回给SpringSecurity
//    UserDetails getUserDetails(String userName);

    //注册学生的方法
    //因为使用异常表示运行的状态，所以返回值直接使用void
    //参数方面，控制器接收到了RegisterVo对象
    void registerStudent(RegisterVo registerVo);

    //查询所有老师
    List<User> getMasters();

    //获得所有老师Map的方法
    Map<String , User> getMastersMap();

    //获得当前登录信息面板的方法
    UserVo getCurrentUserVo(String username);



    //微服务Rest接口中需要使用的方法
    //用户名获取用户信息(因为其他微服务只能调用控制层，而控制层只能调用数据访问层，而数据访问层才能调用微服务)

    User getUserByUsername(String username);

    //根据用户id获取用户所有权限
    List<Permission> getUserPermission(Integer userId);
    //获取用户角色
    List<Role> getUserRoles(Integer userId);
}
