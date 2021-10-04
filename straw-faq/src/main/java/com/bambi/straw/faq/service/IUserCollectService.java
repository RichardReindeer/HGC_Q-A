package com.bambi.straw.faq.service;


import com.bambi.straw.commons.model.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
public interface IUserCollectService extends IService<UserCollect> {

    //根据用户ID查询用户收藏的问题数
    Integer countQuestionCollectionByUserId(Integer userId);
}
