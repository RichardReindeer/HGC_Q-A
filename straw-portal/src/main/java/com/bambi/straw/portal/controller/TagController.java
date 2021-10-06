package com.bambi.straw.portal.controller;


import com.bambi.straw.portal.model.Tag;
import com.bambi.straw.portal.service.ITagService;
import com.bambi.straw.portal.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/v1/tags")
public class TagController {

    @Autowired
    private ITagService tagService;

    //@GetMapping("")表示当前方法的路径就是"/v1/tags"
    @GetMapping("")
    public R<List<Tag>> tags(){
        //调用业务逻辑层代码来获取所有标签
        List<Tag> tags = tagService.getTags();
        //利用R对象返回查询到的所有标签
        return R.ok(tags);
    }
}
