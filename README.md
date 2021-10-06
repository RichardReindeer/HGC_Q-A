创建代码生成器子项目  
    straw - generator
<br>
将UserController的requestMapping路径改为v1/users 为了后期转为微服务项目
<br>
给portal添加security依赖
<p>
    实现密码加密:
    <br>
    properties文件中保存用户的用户名和密码没有任何安全性<br>
    我们希望通过密码加密，保存密码，让项目更安全 <br>
    密码加密 ——>即根据一定规则将密码加密为密文字符串的操作
</p>
<h2>特殊权限</h2>
<p>
    每个用户可以有不同的权限，通过权限去分别限制不同用户的工作 <br>
    (毕设需要对权限进行重新设计) <br>
    我们可以在java代码中配置用户的信息<br>
    让这个用户拥有指定的权限 <br>
    <h/>
    创建一个Spring-Security的配置类 <br>
    在这个类中配置相关的信息和权限 <br>
    在项目中创建一个Security包，再创建个类
</p>
<h2> 用户登录功能</h2>
<p>
    数据访问层<br>
    在用户mapper类中编写根据id返回用户权限，和根据用户名查询用户信息的方法<br>
    在Test中进行测试<br>
    service层<br>
    在接口中写出根据用户名返回UserDetails的方法<br>
    在实现类中实现<br><br>
    <b>在SpringSecurity下调用业务逻辑层方法</b> <br>
    只需要按照规定的方式调用即可<br>
    创建一个类实现Spring-Security提供的接口<br>
    UserDetailsService
</p>
<p>
    设置放行页面
</p>
