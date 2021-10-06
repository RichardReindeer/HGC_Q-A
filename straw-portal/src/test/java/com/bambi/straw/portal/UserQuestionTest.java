package com.bambi.straw.portal;

import com.bambi.straw.portal.service.IUserService;
import com.bambi.straw.portal.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserQuestionTest {
    @Autowired
    IUserService userService;
    @Test
    void getUserVo(){
        UserVo userVo = userService.getCurrentUserVo("st2");
        log.debug("{}",userVo);
    }
}
