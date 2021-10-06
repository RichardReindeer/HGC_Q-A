package com.bambi.straw.portal.service;

import com.bambi.straw.portal.model.Comment;
import com.bambi.straw.portal.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
public interface ICommentService extends IService<Comment> {
    //新增评论的方法
    Comment saveComment(CommentVo commentVo, String username);

    /**
     * 控制层只能判断是身份，但是这个评论删除功能不仅是根据身份进行判断的
     *
     * @param commentId
     * @param username
     * @return
     */

    //按id删除评论的方法
    boolean removeComment(Integer commentId, String username);

    //按id修改评论的方法
    Comment updateComment(Integer commentId, String content, String username);
}
