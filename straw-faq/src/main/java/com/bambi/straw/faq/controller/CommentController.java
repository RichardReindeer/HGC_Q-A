package com.bambi.straw.faq.controller;

import com.bambi.straw.commons.model.Comment;
import com.bambi.straw.commons.vo.R;
import com.bambi.straw.faq.service.ICommentService;
import com.bambi.straw.faq.vo.CommentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：
 * 回复类请求前端控制器类
 * <pre>
 * HISTORY
 * ****************************************************************************
 *  ID     DATE          PERSON          REASON
 *  1      2021/10/6 21:08    Bambi        Create
 * ****************************************************************************
 * </pre>
 *
 * @author Bambi
 * @since 1.0
 */
@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private ICommentService commentService;

    /**
     * 对回复进行保存
     *
     * @param commentVo   接收到的回复信息
     * @param result      验证
     * @param userDetails 用户信息
     * @return 标准R类
     */
    @RequestMapping
    public R<Comment> postComment(
            @Validated CommentVo commentVo, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("postComment is starting ! ! !");
        if (result.hasErrors()) {
            logger.error("has some problems in postComment , result has Error");
            return R.unproecsableEntity(result.getFieldError().getDefaultMessage());
        }
        logger.info("postComment is finish");
        return R.created(commentService.saveComment(commentVo, userDetails.getUsername()));
    }

    /**
     * 删除回复
     *
     * @param id          回复id
     * @param userDetails 用户详情
     * @return
     */
    @RequestMapping("/{id}/delete")
    public R removeComment(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("removeComment is Starting ! ! !");
        if (id == null) {
            logger.error("id is null");
            return R.notFound("id 不存在");
        }
        boolean isDelete = commentService.removeComment(id, userDetails.getUsername());
        if (isDelete) {
            logger.info("删除成功");
            return R.gone("删除成功");
        } else {
            logger.error("删除失败，check the pathVal");
            return R.notFound("删除失败");
        }
    }


    /**
     * 对回复进行修改
     * @param id
     * @param content
     * @param userDetails
     * @return
     */
    @RequestMapping("/{id}/update")
    public R<Comment> updateComment(
            @PathVariable Integer id,
            String content,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("updateComment is starting ! !");
        if (id == null) {
            logger.error("id is not found");
            return R.notFound("id is not found");
        }
        if (userDetails == null) {
            logger.error("userDetails is null");
            return R.notFound("userDetails is null");
        }

        return R.ok(commentService.updateComment(id, content, userDetails.getUsername()));
    }

}
