package com.bambi.straw.faq.service.impl;


import com.bambi.straw.commons.model.UserCollect;
import com.bambi.straw.faq.mapper.UserCollectMapper;
import com.bambi.straw.faq.service.IUserCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {
    @Autowired
    private UserCollectMapper userCollectMapper;
    @Override
    public Integer countQuestionCollectionByUserId(Integer userId) {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        //查询到的收藏数
        Integer count = userCollectMapper.selectCount(queryWrapper);
        return count;
    }
}
