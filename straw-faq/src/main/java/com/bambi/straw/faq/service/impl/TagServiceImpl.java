package com.bambi.straw.faq.service.impl;


import com.bambi.straw.commons.model.Tag;
import com.bambi.straw.faq.mapper.TagMapper;
import com.bambi.straw.faq.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    /**
     * 因为Spring底层创建对象是单例的，所以只会创建一个对象，这个属性也只会生成一个
     */

    //需要声明一个成员变量来保存所有标签
    //因为这个变量可能同时被多个线程使用，所以要考虑线程安全问题
    //CopyOnWriteArrayList不仅是线程安全的，还在保证线程安全的同时确保的运行的速度
    //Vactor是线程安全的 但是效率太低
    private List<Tag> tags = new CopyOnWriteArrayList<>();//这个是线程安全的集合
    //private RedisTemplate<String,List<Tag>> tags ;

    //因为已经缓存了list，现在需要一个map，也使用缓存的方式处理即可
    //要定义缓存，就需要在属性位置声明一个Map
    //ConcurrentHashMap是线程安全的HashMap
    private Map<String,Tag> name2TagMap = new ConcurrentHashMap<>();


    @Override
    public List<Tag> getTags() {
        /**
          要先判断tags是否为空，如果是空才需要连接数据库查询
         **/
        if(tags.isEmpty()){
            //为了防止多条线程都进行新增操作，这里进行加锁，

            //利用list方法向tags赋值
            synchronized (tags){
                //在加锁的情况下判断tags是不是空
                //如果在加锁的情况下还是空，才是真正的第一次请求
                if(tags.isEmpty()) {
                    tags.addAll(list());
                    //下面给map赋值
                    tags.forEach(tag -> name2TagMap.put(tag.getName(),tag));
                }
            }
        }
        return tags;
    }


    @Override
    public Map<String, Tag> getName2TagMap() {
        if(name2TagMap.isEmpty()){
            getTags();
        }
        return name2TagMap;
    }
}
