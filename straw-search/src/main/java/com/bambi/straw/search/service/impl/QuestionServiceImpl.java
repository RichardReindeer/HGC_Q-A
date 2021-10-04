package com.bambi.straw.search.service.impl;

import com.bambi.straw.commons.model.Question;
import com.bambi.straw.commons.model.Tag;
import com.bambi.straw.commons.model.User;
import com.bambi.straw.search.repository.QuestionRepository;
import com.bambi.straw.search.service.IQuestionService;
import com.bambi.straw.search.vo.Pages;
import com.bambi.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class QuestionServiceImpl implements IQuestionService , Serializable {

    //为了实现ES访问
    @Resource
    private QuestionRepository questionRepository;
    @Autowired
    private RestTemplate restTemplate;



    @Override
    public void sync() {
        //先获得总页数以确定循环次数
        String url = "http://faq-service/v1/questions/page/count?pageSize={1}";
        Integer pageSize = 8;
        int totalPage = restTemplate.getForObject(url,Integer.class,pageSize);
        //totalPage是总页数也是循环次数,
        //因为Faq那边使用的是Mybatis进行的分页，1代表第一页，所以需要i+1
        for(int i =0;i<totalPage;i++){
            url = "http://faq-service/v1/questions/page?pageNum={1}&pageSize={2}";
            QuestionVo[] questionVos = restTemplate.getForObject(url,QuestionVo[].class,i+1,pageSize);
            questionRepository.saveAll(Arrays.asList(questionVos));
            log.debug("保存了第{}页,",i+1);
        }
    }

    //通过用户名获取用户对象的方法
    private User getUser(String username){
        String url =  "http://sys-service/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(url,User.class,username);
        return user;
    }

    @Override
    public PageInfo<QuestionVo> search(String key, String username, Integer pageNum, Integer pageSize) {
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=8;
        }
        //获取用户
        User user = getUser(username);
        //设定分页查询的页码， 每页条数以及按时间降序
        Pageable pageable = PageRequest.of(pageNum-1,pageSize, Sort.Direction.DESC,"createtime");
        Page<QuestionVo> page = questionRepository.queryAllByParams(key,key,user.getId(),pageable);
        //获得包含所有标签的map
        Map<String , Tag> name2TagMap = getName2TagMap();
        //循环遍历查询出的所有QuestionVo向Tags中复制
        page.getContent().forEach(questionVo -> {
            List<Tag> tags = tagsNamesToTags(questionVo.getTagNames());
            questionVo.setTags(tags);
        });
        return Pages.pageInfo(page);
    }

    private final Map<String , Tag> name2TagMap = new ConcurrentHashMap<>();
    private Map<String , Tag> getName2TagMap(){
        if(name2TagMap.isEmpty()){
            String url = "http://faq-service/v1/tags/list";
            Tag[] tags = restTemplate.getForObject(url,Tag[].class);
            for(Tag tag : tags){
                name2TagMap.put(tag.getName(),tag);
            }
        }
        return name2TagMap;
    }


    /**
     * 将标签名列表转换为标签列表的集合
     */
    private List<Tag> tagsNamesToTags(String tagsNames){
        String[] names = tagsNames.split(",\\s?");
        Map<String,Tag> name2TagMap = getName2TagMap();
        List<Tag> tags = new ArrayList<>();
        for(String name : names){
            Tag tag = name2TagMap.get(name);
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public void saveQuestion(QuestionVo questionVo) {
        questionRepository.save(questionVo);
    }
}
