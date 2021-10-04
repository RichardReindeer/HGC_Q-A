package com.bambi.straw.gateway.service;

import com.bambi.straw.commons.model.Permission;
import com.bambi.straw.commons.model.Role;
import com.bambi.straw.commons.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务逻辑层特征:
 * 被控制层调用
 * 调用数据访问层
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.调用根据用户名获得用户对象的rest接口
        //{1} 1为参数
        String url = "http://sys-service/v1/auth/user?username={1}";
        //从第三个开始，就开始给参数赋值了哦 ，看源码， 因为传入的参数是可变参数
        /*
            getForObject 方法第三个参数开始时传入url{}占位符用的
         */
        User user = restTemplate.getForObject(url, User.class, username);
        log.debug("查询出用户:{}", user);
        //2.判单用户不为空
        if (user == null) {
            //自带异常
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //3.调用根据用户id获得用户权限的Rest接口
        url = "http://sys-service/v1/auth/permissions?userId={1}";
        //凡是Rest接口返回List格式的，调用的时候使用对应泛型的数组类型接收
        //因为传递过程中数据是json格式，json格式是js代码， js代码没有List类型，只有数组类型
        Permission[] permissions = restTemplate.getForObject(url, Permission[].class, user.getId());
        //4.调用根据用户id获取的用户角色的Rest接口
        url = "http://sys-service/v1/auth/roles?userId={1}";
        Role[] roles = restTemplate.getForObject(url, Role[].class, user.getId());
        //5.将权限和角色赋值到auth数组中
        if (permissions == null || roles == null) {
            throw new UsernameNotFoundException("权限或角色身份异常");
        }
        //声明保存所有权限和角色的数组
        String[] auth = new String[permissions.length + roles.length];
        int index = 0;
        for(Permission permission : permissions){
            //赋权限
            auth[index++] = permission.getName();
        }
        for(Role role:roles){
            auth[index++] = role.getName();
        }

        //6.构造UserDetails对象 并返回
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                .accountLocked(user.getLocked()==1)
                .disabled(user.getEnabled()==0)
                .build();

        return userDetails;
    }
}
