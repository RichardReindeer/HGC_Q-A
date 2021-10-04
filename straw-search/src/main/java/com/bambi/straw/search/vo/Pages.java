package com.bambi.straw.search.vo;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class Pages {
    /**
     * 将Spring-Data提供的翻页数据，转换为Pagehelper翻页数据对象
     * @param page Spring-Data提供的翻页数据
     * @return PageInfo
     */
    public static <T> PageInfo<T> pageInfo(Page<T> page){
        //当前页号从1开始， Spring-Data从0开始，所以要加1
        int pageNum = page.getNumber()+1;
        //当前页面大小
        int pageSize = page.getSize();
        //总页数 pages
        int pages = page.getTotalPages();
        //当前页面中数据
        List<T> list = new ArrayList<>(page.toList());
        //当前页面实际数据大小，有可能能小于页面大小
        int size = page.getNumberOfElements();
        //当前页的第一行在数据库中的行号, 这里从0开始
        int startRow = page.getNumber()*pageSize;
        //当前页的最后一行在数据库中的行号, 这里从0开始
        int endRow = page.getNumber()*pageSize+size-1;
        //当前查询中的总行数
        long total = page.getTotalElements();

        PageInfo<T> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages(pages);
        pageInfo.setStartRow(startRow);
        pageInfo.setEndRow(endRow);
        pageInfo.setSize(size);
        pageInfo.setTotal(total);
        pageInfo.calcByNavigatePages(PageInfo.DEFAULT_NAVIGATE_PAGES);

        return pageInfo;
    }
}
