package com.bambi.straw.search.repository;

import com.bambi.straw.search.vo.QuestionVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends ElasticsearchRepository<QuestionVo,Integer> {

    //按搜索业务逻辑查询ES中问题的方法
    @Query("{\n" +
            "    \"bool\": {\n" +
            "      \"must\": [{\n" +
            "        \"bool\": {\n" +
            "          \"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"content\": \"?1\"}}]\n" +
            "        }\n" +
            "      }, {\n" +
            "        \"bool\": {\n" +
            "          \"should\": [{\"term\": {\"publicStatus\": 1}}, {\"term\": {\"userId\": ?2}}]\n" +
            "        }\n" +
            "      }]\n" +
            "    }\n" +
            "  }")
    public Page<QuestionVo> queryAllByParams(
            String title,String content , Integer userId, Pageable pageable
            );


}
