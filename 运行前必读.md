# 注意

- 前端页面在gateWay中，static内缺少bower_components文件夹，因为上传不上去
- Kafka 需要分区 search.questions 请自行创建
- Es 索引库对应实体类上标签请自行查看
- 启动项目前请确定zookeeper、kafka、redis、es都已正常运行
- 切换新的索引库时，请调用search测试包中的sync方法进行索引库同步
- 记得改数据库的密码

