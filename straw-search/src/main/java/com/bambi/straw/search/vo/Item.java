package com.bambi.straw.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Accessors(chain = true)
//全参数构造器(业务需要和框架无关)
@AllArgsConstructor
//无参构造器
@NoArgsConstructor
//SpringDataEs设置的对应ES所以索引的注解
//在注解的参数中确定索引的名称，使用这个索引时，若索引不存在，会先自动创建

@Document(indexName = "items")
public class Item implements Serializable {
    //使用@Id注解来标注其为主键
    @Id
    private Long id ;
    //只有text类型可以支持分词，
    //设置分词器，以及搜素分词器
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String title;
    //需要分词的类型是FileType.Text
    //不需要分词的类型是FileType.Keyword
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String brand;
    @Field(type = FieldType.Double)
    private Double price;
    //index = false 表示不生成这个字段的索引，通常用于不会用于条件查询的列
    @Field(type = FieldType.Keyword,index = false)
    private String images;

}
