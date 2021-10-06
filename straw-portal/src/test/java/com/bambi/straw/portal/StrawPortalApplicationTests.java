package com.bambi.straw.portal;

import com.bambi.straw.portal.mapper.UserMapper;
import com.bambi.straw.portal.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class StrawPortalApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void setUserMapper(){
       User user =  userMapper.selectById(1);
        System.out.println("user = " + user);
    }
}
