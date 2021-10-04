package com.bambi.straw.search;

import com.bambi.straw.search.repository.ItemRepository;
import com.bambi.straw.search.vo.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootTest
class StrawSearchApplicationTests {

    @Resource
    ElasticsearchOperations elasticsearchOperations;
    @Resource
    ItemRepository itemRepository;
    @Test
    void contextLoads() {
        System.out.println("elasticsearchOperations = " + elasticsearchOperations);
    }

    //测试ItemRepository接口中的方法
    @Test
    void setItemRepository(){
        Item item = new Item()
                .setId(1L)
                .setTitle("罗技无线轻量游戏鼠标")
                .setBrand("罗技")
                .setCategory("鼠标")
                .setPrice(799.00)
                .setImages(null);
        itemRepository.save(item);
        System.out.println("新增完成");
    }

    //批量增
    @Test
    void addSoMany(){
        List<Item> items = new CopyOnWriteArrayList<>();
        items.add(new Item(2L,"杜伽银轴机械键盘","键盘","杜伽",699.0,null));
        items.add(new Item(3L,"雷蛇有线电竞鼠标","鼠标","雷蛇",499.0,null));
        items.add(new Item(4L,"索尼定焦半画幅饼干镜头","镜头","索尼",2399.0,null));
        items.add(new Item(5L,"樱桃静音红轴机械键盘","键盘","樱桃",399.0,null));
        items.add(new Item(6L,"飞科三头电动剃须刀","剃须刀","飞科",99.0,null));
        items.add(new Item(7L,"华硕玩家国度高端电竞笔记本","笔记本","华硕",7999.0,null));
        items.add(new Item(8L,"富士高端可调焦距相机","相机","索尼",23999.0,null));
        itemRepository.saveAll(items);
        System.out.println("输出成功");
    }

    //3.全查(id查找)
    @Test
    void findItem(){
        //单查，按照id
        Optional<Item> item = itemRepository.findById(5L);
        System.out.println(item.get());

        Iterable<Item> items = itemRepository.findAll();
        items.forEach(item1 -> System.out.println(item1));
    }

    //4.模糊查询(全文搜索)
    @Test
    void matches(){
        Iterable<Item> items = itemRepository.queryItemsByTitleMatches("键盘");
        items.forEach(item -> System.out.println(item));
    }
    @Test
    void matches2(){
        Iterable<Item>items = itemRepository.queryItemsByTitleMatchesAndBrandMatches("镜头","索尼");
        items.forEach(item -> System.out.println(item));
    }

    @Test
    void matcher3(){
        Iterable<Item> items = itemRepository.queryItemsByTitleMatchesOrderByPriceDesc("鼠标");
        items.forEach(item -> System.out.println(item));
    }

    @Test
    void page(){
        //page将0当作第一页，所以查询到时候要将页数-1
        int pageNum = 1;
        int pageSize = 1;
        Page<Item> page = itemRepository.queryItemsByTitleMatchesOrderByPriceDesc("鼠标", PageRequest.of(pageNum-1,pageSize));
        page.getContent();//获得查询结果的list;
        //page也支持直接遍历
        page.forEach(item -> System.out.println(item));

        //page对象中的常用属性
        System.out.println("总页数 = " + page.getTotalPages());
        System.out.println("当前页数 = " + page.getNumber());
        System.out.println("每页大小 = " + page.getSize());
        System.out.println("是不是第一页 = " + page.isFirst());
        System.out.println("是不是最后一页 = " + page.isLast());
        System.out.println("下一页页码 = " + page.nextOrLastPageable().getPageNumber());
        System.out.println("上一页页码 = " + page.previousOrFirstPageable().getPageNumber());
    }
}
