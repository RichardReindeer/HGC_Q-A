package com.bambi.straw.search.repository;

import com.bambi.straw.search.vo.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    //ElasticSearchRepository<>中两个类型
    //第一个是实体类型，第二个是这个实体类对应的ID的类型
    //继承的这个父接口会自动生成针对Item实体类的CRUD方法

    //SpringData家族查询的返回值(List)都是Iterable
    Iterable<Item> queryItemsByTitleMatches(String title);

    Iterable<Item> queryItemsByTitleMatchesAndBrandMatches(String title,String brand);

    Iterable<Item> queryItemsByTitleMatchesOrderByPriceDesc(String title);

    //分页查询 在方法名是看不出来的 看的是参数
    Page<Item> queryItemsByTitleMatchesOrderByPriceDesc(String title , Pageable pageable);

}
