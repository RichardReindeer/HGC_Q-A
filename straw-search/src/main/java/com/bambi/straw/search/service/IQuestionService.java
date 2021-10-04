package com.bambi.straw.search.service;


import com.bambi.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;

public interface IQuestionService {
    //同步数据库和ES中数据的方法
    void sync();

    //根据关键字查询问题列表的方法
    PageInfo<QuestionVo> search(String key , String username , Integer pageNum , Integer pageSize);

    //向ES中添加数据
    void saveQuestion(QuestionVo questionVo);
}
