package com.bambi.straw.portal.service;

import com.bambi.straw.portal.model.Tag;
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
public interface ITagService extends IService<Tag> {

    //查询所有标签的方法(适合循环遍历)
    List<Tag> getTags();

    //查询所有标签在Map中的方法
    Map<String,Tag> getName2TagMap();

}
