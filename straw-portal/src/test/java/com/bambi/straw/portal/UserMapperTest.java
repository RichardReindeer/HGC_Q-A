package com.bambi.straw.portal;

import com.bambi.straw.portal.mapper.UserMapper;
import com.bambi.straw.portal.model.Permission;
import com.bambi.straw.portal.model.Role;
import com.bambi.straw.portal.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;
    @Test
    void selectPermission(){
        List<Permission> permissions = userMapper.findUserPermissionsById(4);
        permissions.forEach(permission -> System.out.println("permission = " + permission));
    }

    @Test
    void login(){
        String username = "st2";
        User user = userMapper.findUserByUserName(username);
        System.out.println("user = " + user);

        //五表联查
        List<Permission> permissions = userMapper.findUserPermissionsById(user.getId());
        permissions.forEach(permission -> System.out.println("permission = " + permission));
    }
    @Test
    void roleById(){
        List<Role> roles = userMapper.findUserRolesById(11);
        roles.forEach(role -> System.out.println("role = " + role));
    }
}
