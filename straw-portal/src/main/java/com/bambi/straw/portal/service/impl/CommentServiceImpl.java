package com.bambi.straw.portal.service.impl;

import com.bambi.straw.portal.mapper.UserMapper;
import com.bambi.straw.portal.model.Comment;
import com.bambi.straw.portal.mapper.CommentMapper;
import com.bambi.straw.portal.model.User;
import com.bambi.straw.portal.service.ICommentService;
import com.bambi.straw.portal.service.ServiceException;
import com.bambi.straw.portal.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public Comment saveComment(CommentVo commentVo, String username) {
        User user = userMapper.findUserByUserName(username);
        Comment comment = new Comment()
                .setAnswerId(commentVo.getAnswerId())
                .setContent(commentVo.getContent())
                .setCreatetime(LocalDateTime.now())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname());
        int num = commentMapper.insert(comment);
        if (num != 1) {
            throw ServiceException.busy();
        }
        return comment;
    }

    //删除评论
    @Override
    public boolean removeComment(Integer commentId, String username) {
        //获得用户信息
        User user = userMapper.findUserByUserName(username);
        //判断是不是老师
        /*
        包装类进行==判断的时候，底层在编译的时候会变成.equals
        &&是短路判断，前面不符合，就不执行(这是基础啊)
         */
        if (user.getType() != null && user.getType() == 1) {
            int num = commentMapper.deleteById(commentId);
            return num == 1;
        }
        //判断当前登录用户是不是评论的发布者 <---这个操作需要连库，会有较大开销，所以可以先进行身份判断(不用连库)
        //先查询当前评论的详情信息
        Comment comment = commentMapper.selectById(commentId);
        //判断
        if (user.getId().equals(comment.getUserId())) {
            //可以删除自己发布的评论
            int num = commentMapper.deleteById(commentId);
            return num == 1;
        }
        //不符合删除标准可以直接返回异常
        throw ServiceException.invalidRequest("权限不足");
    }

    //修改评论
    @Override
    public Comment updateComment(Integer commentId, String content, String username) {

        //查询当前用户的信息
        User user = userMapper.findUserByUserName(username);
        //查询当前评论的信息
        Comment comment = commentMapper.selectById(commentId);
        //判断是不是老师或是不是评论发布者
        if (user.getType() != null && user.getType() == 1
                || user.getId().equals(comment.getUserId())
        ) {
            //执行修改
            comment.setContent(content);
            int num = commentMapper.updateById(comment);
            if(num != 1){
                throw ServiceException.busy();
            }
            return comment;
        }
        throw ServiceException.invalidRequest("没有权限");
    }
}
