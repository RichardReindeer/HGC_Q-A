package com.bambi.straw.portal.service.impl;

import com.bambi.straw.portal.mapper.QuestionTagMapper;
import com.bambi.straw.portal.mapper.UserMapper;
import com.bambi.straw.portal.mapper.UserQuestionMapper;
import com.bambi.straw.portal.model.*;
import com.bambi.straw.portal.mapper.QuestionMapper;
import com.bambi.straw.portal.service.IQuestionService;
import com.bambi.straw.portal.service.ITagService;
import com.bambi.straw.portal.service.IUserService;
import com.bambi.straw.portal.service.ServiceException;
import com.bambi.straw.portal.vo.QuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    //根据用户名获取用户信息
    @Autowired
    private UserMapper userMapper;
    //获取查询到的问题
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize) {
        //1.根据用户名查询用户对象
        User user = userMapper.findUserByUserName(username);
        //2.根据用户对象设置查询question的条件
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.eq("delete_status",0);
        queryWrapper.orderByDesc("createtime");//根据时间降序
        //3.执行查询返回结果
        //执行分页命令
        //PageHelper提供的方法startPage规定查询第几页，每页多少条
        //同一个方法中没有线程安全问题
        PageHelper.startPage(pageNum,pageSize);
        List<Question> list = questionMapper.selectList(queryWrapper);
        //不要忘记返回list
        log.debug("当前用户的问题数:{}",list.size());
        //遍历所有查询出来的问题，将每个问题的标签列表都赋上值
        list.forEach(question -> {
            //调用我们编写的根据TagNames获得tags的方法
            List<Tag> tags = tagNames2Tags(question.getTagNames());
            question.setTags(tags);
        });
        //实例化PageInfo 会自动计算分页数据
        //List是构造方法的参数，会将这个list保存在pageInfo中
        return new PageInfo<Question>(list);
    }

    @Autowired
    private ITagService tagService;

    //根据tag_names列的值获得它对应的标签集合的方法
    private List<Tag> tagNames2Tags(String tagNames){
        //对tagNames进行字符串的拆分
        String[] names = tagNames.split(",");
        //names:{"","",""}
        //声明一个list用于接收结果
        List<Tag> tags = new ArrayList<>();
        //获得包含所有标签的Map
        Map<String,Tag> name2TagMap = tagService.getName2TagMap();
        //遍历names数组，从name2TagMap获得tag对象赋值给Tags
        for(String name : names){
            tags.add(name2TagMap.get(name));
        }
        //不要忘记return
        return tags;
    }

    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Autowired
    private UserQuestionMapper userQuestionMapper;
    @Autowired
    private IUserService userService;
    @Override
    //添加了声明式事务，如果发生异常，本次运行所有数据库操作都会撤销
    @Transactional
    public void saveQuestion(QuestionVo questionVo, String username) {
        //1.获得用户信息
            User user = userMapper.findUserByUserName(username);
            log.debug("打桩输出,获取当前用户信息{}",user);
        //2.根据用户选择的标签获得标签的字符串tagNames
            StringBuilder stringBuilder = new StringBuilder();
            for(String tagName : questionVo.getTagNames()){
                stringBuilder.append(tagName).append(",");
            }
            //deleteCharAt删除指定下标的字符，最后一个","的位置是字符串长度-1
        String tagNames = stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
        //3.收集信息构建Question对象
        Question question = new Question()
                .setTitle(questionVo.getTitle())
                .setContent(questionVo.getContent())
                .setTagNames(tagNames)
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setDeleteStatus(0)//初始值为0
                .setStatus(0)//初始值为0
                .setPublicStatus(0)//初始值为0
                .setPageViews(0)//初始值为0
                .setCreatetime(LocalDateTime.now());
        //4.执行新增操作
        int num = questionMapper.insert(question);
        if(num!=1){
            throw ServiceException.busy();
        }
        log.debug("数据库添加对象:{}",question);
        //5.新增问题和标签的关系
            //根据标签名获取到标签对象，根据标签对象获取标签id增加到数据库中
            //获得包含所有标签的Map集合
        Map<String,Tag> name2TagMap = tagService.getName2TagMap();
        //有了上面的集合，就方便通过标签名称获得标签对象了
        for(String tagName : questionVo.getTagNames()){
            //获得当前标签名称对应的标签对象
            Tag tag = name2TagMap.get(tagName);
            QuestionTag questionTag = new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(tag.getId());
            num = questionTagMapper.insert(questionTag);
            if(num!=1){
                throw ServiceException.busy();
            }
            log.debug("数据库添加问题标签关系对象:{}",questionTag);
        }
        //6.新增问题和讲师的关系
        //获得包含所有讲师的map
        Map<String , User> masterMap = userService.getMastersMap();
        for(String nickname : questionVo.getTeacherNicknames()){
            User master = masterMap.get(nickname);
            log.debug("------------------------{}",master);
            UserQuestion userQuestion = new UserQuestion()
                    .setQuestionId(question.getId())
                    .setUserId(master.getId())
                    .setCreatetime(LocalDateTime.now());
            num = userQuestionMapper.insert(userQuestion);
            if(num!=1){
                throw  ServiceException.busy();
            }
            log.debug("新增问题教师关系对象:{}",userQuestion);
        }

    }

    @Override
    public int countQuestionByUserId(Integer userId) {
        QueryWrapper<Question> query = new QueryWrapper<>();
        //输入要判断相等的条件
        query.eq("user_id",userId);
        query.eq("delete_status",0);
        //查询问题数
        Integer count = questionMapper.selectCount(query);
        return count;
    }

    @Override
    public PageInfo<Question> getQuestionByTeacherName(String teacherName, Integer pageNum, Integer pageSize) {
        //获得用户信息
        User user = userMapper.findUserByUserName(teacherName);
        //执行分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions = questionMapper.findTeacherQuestions(user.getId());
        //将每个问题的标签赋值给当前问题的tags属性
        questions.forEach(question -> {
            List<Tag> tags = tagNames2Tags(question.getTagNames());
            question.setTags(tags);
        });
        return new PageInfo<>(questions);
    }

    @Override
    public Question getQuestionById(Integer id) {
        //1.根据MybatisPlus方法查询基本信息
        Question question = questionMapper.selectById(id);
        //2.根据问题的所有标签的字符串获得所有标签对象并赋值返回
        List<Tag> tags = tagNames2Tags(question.getTagNames());
        question.setTags(tags);
        return question;
    }

}
