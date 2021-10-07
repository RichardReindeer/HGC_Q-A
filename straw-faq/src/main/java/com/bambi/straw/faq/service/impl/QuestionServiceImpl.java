package com.bambi.straw.faq.service.impl;

import com.bambi.straw.commons.model.*;
import com.bambi.straw.commons.service.ServiceException;
import com.bambi.straw.faq.kafka.KafkaProducer;
import com.bambi.straw.faq.mapper.QuestionMapper;
import com.bambi.straw.faq.mapper.QuestionTagMapper;
import com.bambi.straw.faq.mapper.UserQuestionMapper;
import com.bambi.straw.faq.ribbon.RibbonClient;
import com.bambi.straw.faq.service.IQuestionService;
import com.bambi.straw.faq.service.ITagService;
import com.bambi.straw.faq.vo.QuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述： 问题相关业务逻辑层(已重构)
 *
 * <pre>
 * HISTORY
 * ****************************************************************************
 *  ID     DATE          PERSON          REASON
 *  1      2021/10/7 1:33    Bambi        Create
 * ****************************************************************************
 * </pre>
 *
 * @author Bambi
 * @since 1.0
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    private static Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    //根据用户名获取用户信息
    //获取查询到的问题
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Resource
    private RibbonClient ribbonClient;
    @Resource
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Autowired
    private UserQuestionMapper userQuestionMapper;
    @Autowired
    private ITagService tagService;

    /**
     * 获取用户问题
     * 根据时间降序排序
     * 根据用户名查找到对应用户对象，使用pageHelper执行分页命令进行分页操作
     * 将每个问题的标签都进行赋值 --> 调用根据TagNames获取tags的方法
     * 实例化PageInfo会自动计算分页数据
     *
     * @param username 用户姓名
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @return
     */
    @Override
    public PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize) {
        logger.info("getMyQuestions is starting ! ! !");
        User user = ribbonClient.getUser(username);
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("delete_status", 0);
        queryWrapper.orderByDesc("createtime");//根据时间降序
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectList(queryWrapper);
        logger.debug("当前用户的问题数为:{}", list.size());
        list.forEach(question -> {
            List<Tag> tags = tagNames2Tags(question.getTagNames());
            question.setTags(tags);
        });
        logger.info("getMyQuestions is ending");
        return new PageInfo<Question>(list);
    }


    /**
     * 根据tag_names的值获取其对应标签集合
     *
     * @param tagNames 标签名
     * @return
     */
    private List<Tag> tagNames2Tags(String tagNames) {
        logger.info("tagsNames2Tags is starting ! ! !");
        String[] names = tagNames.split(",");
        List<Tag> tags = new ArrayList<>();
        Map<String, Tag> name2TagMap = tagService.getName2TagMap();
        for (String name : names) {
            tags.add(name2TagMap.get(name));
        }
        logger.debug("this is tagsList {}", tags.toString());
        logger.info("tagsNames2Tags is end");
        return tags;
    }

    /**
     * 保存问题
     * ->获取用户信息，根据用户标签获取标签字符串并拼接
     * ->构建question对象并保存
     * —>进行数据库新增
     * ->新增问题和标签的关系
     * ->新增问题和教师间的关系
     * ->将问题发至kafka
     *
     * @param questionVo 问题参数实体类
     * @param username   用户姓名
     */
    @Override
    @Transactional
    public void saveQuestion(QuestionVo questionVo, String username) {
        User user = ribbonClient.getUser(username);
        logger.debug("this user is {}", user);
        StringBuilder stringBuilder = new StringBuilder();
        for (String tagName : questionVo.getTagNames()) {
            stringBuilder.append(tagName).append(",");
        }
        String tagNames = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

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

        int num = questionMapper.insert(question);
        if (num != 1) {
            throw ServiceException.busy();
        }
        logger.debug("数据库添加对象:{}", question);

        logger.info("获取问题和标签间关系");
        Map<String, Tag> name2TagMap = tagService.getName2TagMap();
        //有了上面的集合，就方便通过标签名称获得标签对象了
        for (String tagName : questionVo.getTagNames()) {
            //获得当前标签名称对应的标签对象
            Tag tag = name2TagMap.get(tagName);
            QuestionTag questionTag = new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(tag.getId());
            num = questionTagMapper.insert(questionTag);
            if (num != 1) {
                throw ServiceException.busy();
            }
            logger.debug("数据库添加问题标签关系对象:{}", questionTag);
        }

        logger.info("获取问题和教师间关系");
        String url = "http://sys-service/v1/users/master";
        User[] users = restTemplate.getForObject(url, User[].class);
        Map<String, User> masterMap = new HashMap<>();
        for (User u : users) {
            masterMap.put(u.getNickname(), u);
        }
        for (String nickname : questionVo.getTeacherNicknames()) {
            User master = masterMap.get(nickname);
            logger.debug("------------------------{}", master);
            UserQuestion userQuestion = new UserQuestion()
                    .setQuestionId(question.getId())
                    .setUserId(master.getId())
                    .setCreatetime(LocalDateTime.now());
            num = userQuestionMapper.insert(userQuestion);
            if (num != 1) {
                throw ServiceException.busy();
            }
            logger.debug("新增问题教师关系对象:{}", userQuestion);
        }

        kafkaProducer.sendQuestion(question);
    }

    /**
     * 统计问题数
     *
     * @param userId 用户id
     * @return 问题总数
     */
    @Override
    public int countQuestionByUserId(Integer userId) {
        logger.info("countQuestionByUserId is starting ! ! ! ");
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        query.eq("delete_status", 0);
        Integer count = questionMapper.selectCount(query);
        logger.info("countQuestionByUserId is ending ");
        return count;
    }

    /**
     * 根据教师姓名获取问题
     *
     * @param teacherName 教师姓名
     * @param pageNum     分页页码
     * @param pageSize    分页大小
     * @return 问题PageInfo
     */
    @Override
    public PageInfo<Question> getQuestionByTeacherName(String teacherName, Integer pageNum, Integer pageSize) {
        logger.info("getQuestionByTeacherName is starting ! ! ! ");
        User user = ribbonClient.getUser(teacherName);
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.findTeacherQuestions(user.getId());
        questions.forEach(question -> {
            List<Tag> tags = tagNames2Tags(question.getTagNames());
            question.setTags(tags);
        });
        logger.debug("this is the size of pageInfo by questions {}", questions.size());
        logger.info("getQuestionByTeacherName is ending");
        return new PageInfo<>(questions);
    }

    /**
     * 根据id获取对应问题
     *
     * @param id
     * @return
     */
    @Override
    public Question getQuestionById(Integer id) {
        logger.info("getQuestionById is starting ! ! ! ");
        Question question = questionMapper.selectById(id);
        List<Tag> tags = tagNames2Tags(question.getTagNames());
        question.setTags(tags);
        logger.info("getQuestionById is ending ");
        return question;
    }


    /**
     * 根据页码和页大小获取问题列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Question> getQuestion(Integer pageNum, Integer pageSize) {
        logger.info("getQuestion is starting ! ! ! ");
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectList(null);
        logger.info("getQuestion is ending");
        return new PageInfo<>(list);
    }

    /**
     * 新增; 根据标签获取对应问题
     *
     * @param username 用户名
     * @param tagNum   标签id
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return
     */
    @Override
    public PageInfo<Question> getQuestionWithTag(String username, Integer tagNum, Integer pageNum, Integer pageSize) {
        logger.info("getQuestionWithTag is starting ");
        if (username == null) {
            logger.error("username 是空");
            return null;
        }
        User user = ribbonClient.getUser(username);
        if (user != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<Question> questionWithTag = questionMapper.findQuestionWithTag(tagNum, user.getId());
            logger.debug("当前用户的问题数:{}", questionWithTag.size());
            questionWithTag.forEach(question -> {
                List<Tag> tags = tagNames2Tags(question.getTagNames());
                question.setTags(tags);
            });
            logger.info("getQuestionWithTag is ending ");
            return new PageInfo<Question>(questionWithTag);
        }
        logger.error("user==null !! in QuestionServiceImpl");
        return null;
    }

    /**
     * 获取热点问题
     *
     * @param username
     * @return
     */
    @Override
    public PageInfo<Question> getHotQuestion(String username) {
        if (Strings.isEmpty(username)) {
            logger.error("userName is null");
            return null;
        }
        User user = ribbonClient.getUser(username);
        List<Question> hotQuestion = questionMapper.findHotQuestion(user.getId());
        logger.info("当前问题数:{}", hotQuestion.size());
        hotQuestion.forEach(question -> {
            List<Tag> tags = tagNames2Tags(question.getTagNames());
            question.setTags(tags);
        });
        return new PageInfo<Question>(hotQuestion);
    }

    @Deprecated
    @Override
    public Integer getCollectQuestion(Integer userId) {
        logger.info("getCollectQuestion is starting ! ! !");
        Integer collectQuestion = questionMapper.findCollectQuestion(userId);
        logger.info("收藏的问题数为:{}", collectQuestion);
        return collectQuestion;
    }

    /**
     * 根据用户信息获取用户收藏的问题
     * 自测通过
     *
     * @param username
     * @return
     */
    @Override
    public PageInfo<Question> getUsersCollectQuestion(String username , Integer pageNum, Integer pageSize) {
        logger.info("GetUserCollectQuestion is starting ! ! !");
        User user = ribbonClient.getUser(username);
        logger.debug("获取到对应user ==============> {}", user.getNickname());
        PageHelper.startPage(pageNum, pageSize);
        List<Question> userCollectQuestions = questionMapper.findUserCollectQuestions(user.getId());
        userCollectQuestions.forEach(question -> {
            List<Tag> tags = tagNames2Tags(question.getTagNames());
            question.setTags(tags);
        });
        logger.info("getUserCollectQuestion is ending ");
        return new PageInfo<Question>(userCollectQuestions);
    }
}
