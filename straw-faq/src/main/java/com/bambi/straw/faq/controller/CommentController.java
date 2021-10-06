package com.bambi.straw.faq.controller;


import com.bambi.straw.commons.model.Comment;
import com.bambi.straw.commons.vo.R;
import com.bambi.straw.faq.service.ICommentService;
import com.bambi.straw.faq.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/v1/comments")
@Slf4j
public class CommentController {

    @Autowired//spring 给的
    private ICommentService commentService;
    @PostMapping
    public R<Comment> postComment(
            @Validated CommentVo commentVo, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        log.debug("收到的评论信息为:{}",commentVo);
        if(result.hasErrors()){
            //获得字符串类型的错误信息
            String message = result.getFieldError().getDefaultMessage();
            //返回不可用实体错误
            return R.unproecsableEntity(message);
        }

        //调用业务逻辑层代码
        Comment comment = commentService.saveComment(commentVo, userDetails.getUsername());
        //返回
        return R.created(comment);
    };


    //删除评论的控制层方法
    @GetMapping("/{id}/delete")
    public R removeComment(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        if(id==null) R.notFound("Id不存在");
        //调用业务逻辑层
        boolean isDelete = commentService.removeComment(id,userDetails.getUsername());
        if(isDelete){
            return R.gone("删除完成");
        }else {
            return R.notFound("删除失败，请检查");
        }
    }

    @PostMapping("/{id}/update")
    public R<Comment> updateComment(
            @PathVariable Integer id,
            String content,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        Comment comment = commentService.updateComment(id,content, userDetails.getUsername());
        return R.ok(comment);
    }

}