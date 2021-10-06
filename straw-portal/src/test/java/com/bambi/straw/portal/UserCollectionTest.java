package com.bambi.straw.portal;

import com.bambi.straw.portal.mapper.UserCollectMapper;
import com.bambi.straw.portal.service.IUserCollectService;
import com.bambi.straw.portal.service.impl.UserCollectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserCollectionTest {
    @Autowired
    private IUserCollectService userCollectService;
    @Test
    void collectionFind(){
        Integer count = userCollectService.countQuestionCollectionByUserId(11);
        log.debug("{}",count);
    }
}
