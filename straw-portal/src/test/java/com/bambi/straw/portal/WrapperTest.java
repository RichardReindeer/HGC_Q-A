package com.bambi.straw.portal;

import com.bambi.straw.portal.mapper.ClassroomMapper;
import com.bambi.straw.portal.model.Classroom;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WrapperTest {
    //使用QueryWrapper查询邀请码
    //根据邀请码查询ClassRoom对象

    @Autowired
    ClassroomMapper classroomMapper;

    @Test
    void queryWrapperTest(){
        //实例化一个QueryWrapper对象
        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        //设置where查询条件 这里使用各种方法表示各种关系运算符
        //邀请码等于一个值   数据库的列！！！
        queryWrapper.eq("invite_code","JSD2001-706246");
        //根据设置的条件进行查询
        //我们可以在运行的时候观察它生成的sql语句
        Classroom classroom = classroomMapper.selectOne(queryWrapper);
        System.out.println("classroom = " + classroom);
    }
}
