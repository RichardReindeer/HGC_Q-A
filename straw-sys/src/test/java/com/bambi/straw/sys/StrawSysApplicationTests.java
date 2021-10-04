package com.bambi.straw.sys;

import com.bambi.straw.commons.model.User;
import com.bambi.straw.sys.mapper.UserMapper;
import com.bambi.straw.sys.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StrawSysApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        User user = userMapper.findUserByUserName("st2");
        System.out.println("user = " + user);
    }

    @Autowired
    IUserService userService;
    @Test
    void getMaster(){
        List<User> users = userService.getMasters();
        users.forEach(user -> System.out.println("user = " + user));
    }
}
