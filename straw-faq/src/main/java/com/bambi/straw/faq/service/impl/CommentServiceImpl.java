package com.bambi.straw.faq.service.impl;


import com.bambi.straw.commons.model.Comment;
import com.bambi.straw.commons.model.User;
import com.bambi.straw.commons.service.ServiceException;
import com.bambi.straw.faq.mapper.CommentMapper;
import com.bambi.straw.faq.ribbon.RibbonClient;
import com.bambi.straw.faq.service.ICommentService;
import com.bambi.straw.faq.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    private static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Resource
    private RibbonClient ribbonClient;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public Comment saveComment(CommentVo commentVo, String username) {
        return save(commentVo, username);
    }

    //删除评论
    @Override
    @Transactional
    public boolean removeComment(Integer commentId, String username) {
        return remove(commentId, username);
    }

    //修改评论
    @Transactional
    @Override
    public Comment updateComment(Integer commentId, String content, String username) {
        return update(commentId, content, username);
    }

    /**
     * 对评论进行更新操作
     * 判断当前用户是否为评论的发布者
     *
     * @param commentId 当前评论的id
     * @param content   评论正文
     * @param username  用户姓名
     * @return
     */
    private Comment update(Integer commentId, String content, String username) {
        logger.info("开始执行评论更新操作");
        User user = ribbonClient.getUser(username);
        if (user == null) {
            logger.info("当前用户名无法查询到用户");
            throw ServiceException.invalidRequest("用户查询失败");
        }

        Comment comment = commentMapper.selectById(commentId);

        if (user.getType() != null && user.getType() == 1
                || user.getId().equals(comment.getUserId())
        ) {

            comment.setContent(content);
            int num = commentMapper.updateById(comment);
            if (num != 1) {
                throw ServiceException.busy();
            }
            return comment;
        }
        throw ServiceException.invalidRequest("没有权限");
    }

    /**
     * 对评论进行删除操作
     * 对用户权限进行判定
     * 判定条件： 删除评论者需是评论人自己或老师
     * 进行删除操作会产生一定的开销，所以事先进行权限判定，如果条件不合适就不用连数据库
     * <p>
     * <p>
     * 当包装类进行==判定时，底层在编译时会自动变为.equals
     *
     * @param commentId 评论id
     * @param username  用户名
     * @return
     */
    private boolean remove(Integer commentId, String username) {
        logger.info("开始执行评论删除操作");
        User user = ribbonClient.getUser(username);
        if (user.getType() != null && user.getType() == 1) {
            int num = commentMapper.deleteById(commentId);
            return num == 1;
        }
        Comment comment = commentMapper.selectById(commentId);
        if (user.getId().equals(comment.getUserId())) {
            int num = commentMapper.deleteById(commentId);
            return num == 1;
        }
        throw ServiceException.invalidRequest("权限不足");
    }

    /**
     * 对新评论进行保存操作，没什么可说的
     *
     * @param commentVo
     * @param username
     * @return
     */
    private Comment save(CommentVo commentVo, String username) {
        logger.info("接收到新评论，save方法开始执行");
        User user = ribbonClient.getUser(username);
        Comment comment = new Comment()
                .setAnswerId(commentVo.getAnswerId())
                .setContent(commentVo.getContent())
                .setCreatetime(LocalDateTime.now())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname());
        int num = commentMapper.insert(comment);
        if (num != 1) {
            logger.error("服务器忙");
            throw ServiceException.busy();
        }
        return comment;
    }
}
