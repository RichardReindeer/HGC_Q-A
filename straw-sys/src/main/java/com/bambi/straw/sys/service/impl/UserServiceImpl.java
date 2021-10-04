package com.bambi.straw.sys.service.impl;


import com.bambi.straw.commons.model.*;
import com.bambi.straw.commons.service.ServiceException;
import com.bambi.straw.sys.mapper.ClassroomMapper;
import com.bambi.straw.sys.mapper.UserMapper;
import com.bambi.straw.sys.mapper.UserRoleMapper;
import com.bambi.straw.sys.service.IUserService;
import com.bambi.straw.sys.vo.RegisterVo;
import com.bambi.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    //新增的，看笔记走
    @Resource
    private ClassroomMapper classroomMapper;
    //注册学生新增的只是用户表，但是想要表示学生这个身份，需要添加user_role表的数据，向关系表中添加数据
    @Autowired
    private UserRoleMapper roleMapper;

    //接口创建新的方法后需要去实现类进行重写
    /*@Override
    public UserDetails getUserDetails(String userName) {
        //要返回用户详情 分两部分:
        *//*
            1.用户密码，
                密码可以使用用户名查询用户对象获得
            2.用户权限
         *//*
        User user = userMapper.findUserByUserName(userName);
        //如果用户不存在则为空，
        if(user==null){
            return null;
        }
        //用户存在的情况下再去查询所有权限
        List<Permission> permissions = userMapper.findUserPermissionsById(user.getId());
        String [] auth = new String[permissions.size()];
        int i = 0;
        for (Permission p : permissions){
            auth[i++]  = p.getName();
        }
        //查询用户所有角色
        List<Role> roles = userMapper.findUserRolesById(user.getId());
        //对auth数组进行扩容 以来存放用户的角色信息
        auth = Arrays.copyOf(auth,auth.length+roles.size());
        for(Role role : roles){
            auth[i++] = role.getName();
        }


        //开始创建UserDetails对象
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(userName).password(user.getPassword())
                //authorities是包含当前用户所有权限的数组
                .authorities(auth)
                //Spring-Security提供了用户锁定或禁用的设置
                //accountLocked(true)表示锁定,不锁定要传入false
                .accountLocked(user.getLocked()==1)
                //disabled中true表示禁用
                .disabled(user.getEnabled()==0)
                .build()
                ;
        //要记得返回userDetails
        return userDetails;
    }*/

    //
    @Override
    public void registerStudent(RegisterVo registerVo) {
        //1.判断RegisterVo对象中的邀请码是否正确
        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invite_code", registerVo.getInviteCode());//与用户填写的验证码比较
        Classroom classroom = classroomMapper.selectOne(queryWrapper);
        log.debug("邀请码查询出的班级为:{}", classroom);
        if (classroom == null) {
            //如果邀请码发生异常，
            throw ServiceException.unprocesabelEntity("邀请码错误");  //这异常自己写的哦
        }
        //2.判断registerVo对象中注册的用户名是否可用
        User user = userMapper.findUserByUserName(registerVo.getPhone());//判断手机号(昵称)
        if (user != null) {
            //不是空则证明已存在，需要抛出异常
            throw ServiceException.unprocesabelEntity("账号已经被注册，请联系相关老师");
        }
        //3.收集各种信息，包括密码加密

        User stu = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        stu.setUsername(registerVo.getPhone());
        stu.setNickname(registerVo.getNickname());
        stu.setPassword("{bcrypt}" + encoder.encode(registerVo.getPassword()));//已经加密的密码 需要搭配一个算法ID，不然会出问题
        stu.setClassroomId(classroom.getId());
        stu.setCreatetime(LocalDateTime.now());//运行程序此时此刻的时分秒
        stu.setLocked(0);
        stu.setEnabled(1);
        //4.执行新增学生的user表
        int num = userMapper.insert(stu);
        if (num != 1) {
            //到数据库的位置却添加不进去数据，则是服务器忙
            throw ServiceException.busy();
        }
        //5.为新增的学生添加user_role表的关系
        UserRole userRole = new UserRole();
        userRole.setUserId(stu.getId());//insert方法会自动将自增的id值赋值到stu的id属性中
        userRole.setRoleId(2);
        num = roleMapper.insert(userRole);
        if (num != 1) {
            throw ServiceException.busy();
        }


    }

    //声明老师的List和Map缓存属性
    private List<User> masters = new CopyOnWriteArrayList<>();
    private Map<String, User> masterMap = new ConcurrentHashMap<>();
    private Timer timer = new Timer();

    //编写一个初始化代码块，来设定每隔30分钟清空一次缓存
    //初始化代码块在构造方法执行之前运行
    {
        //这里的代码会在构造方法执行之前运行
        //1.匿名内部类,2.第一次循环的时间,3.每隔xx时间循环一次
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        //线程安全
                        synchronized (masters) {
                            masters.clear();
                            masterMap.clear();
                            log.debug("缓存已经清空");
                        }
                    }
                }
                , 1000 * 60 * 30, 1000 * 60 * 30);
    }

    @Override
    public List<User> getMasters() {
        if (masters.isEmpty()) {
            synchronized (masters) {
                if (masters.isEmpty()) {
                    QueryWrapper<User> query = new QueryWrapper<>();
                    //type的值为1则为老师
                    query.eq("type", 1);
                    List<User> list = userMapper.selectList(query);
                    //将全部老师保存到list缓存
                    masters.addAll(list);
                    //将全部讲师保存到Map缓存
                    list.forEach(user -> masterMap.put(user.getNickname(), user));
                    //因为密码属性属于安全级别较高的属性
                    //不宜长时间保存在内存中
                    //所以我们编写代码清除密码或类似的"敏感信息"
                    list.forEach(user -> user.setPassword(""));
                }
            }
        }

        return masters;
    }

    @Override
    public Map<String, User> getMastersMap() {
        if (masterMap.isEmpty()) {
            getMasters();
        }
        return masterMap;
    }

    //    @Autowired
//    IQuestionService questionService;
//    @Autowired
//    IUserCollectService userCollectService;
    @Resource
    private RestTemplate restTemplate;

    @Override
    public UserVo getCurrentUserVo(String username) {
        //先根据用户名查询用户信息
        UserVo userVo = userMapper.findUserVoByUserName(username);
//        //根据用户id查询问题数
//            int questions = questionService.countQuestionByUserId(userVo.getId());
//            int collections = userCollectService.countQuestionCollectionByUserId(userVo.getId());
        String url = "http://faq-service/v1/questions/count?userId={1}";
        Integer count = restTemplate.getForObject(url, Integer.class, userVo.getId());
        url = "http://faq-service/v1/userCollects/count?userId={1}";
        Integer collectCount = restTemplate.getForObject(url, Integer.class, userVo.getId());
//        //赋值并返回\
//        userVo.setQuestions(questions);
//        userVo.setCollections(collections);
        userVo.setQuestions(count);
        userVo.setCollections(collectCount);
        return userVo;
    }


    @Override
    public User getUserByUsername(String username) {
        return userMapper.findUserByUserName(username);
    }

    @Override
    public List<Permission> getUserPermission(Integer userId) {
        return userMapper.findUserPermissionsById(userId);
    }

    @Override
    public List<Role> getUserRoles(Integer userId) {
        return userMapper.findUserRolesById(userId);
    }
}
