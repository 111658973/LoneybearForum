#####


<div style="display: flex;align-items: center">
    <img src="https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/run.gif" width="90px" height="90px" align=center>
   <img src="https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/logo_Link.png" align=center height="40px"> 
</div>



***
#####   一个基于Spring-Boot和Vue的网络论坛项目
***
> 作者的本科毕业设计项目，一个前后端分离的论坛项目,现在充其量是个demo，能跑，作者要去考研跑路了，考完研会详细的重构，所以这是一个先行体验版DEMO，而且的确穷跑不起服务器

### 简要介绍
- 本项目是作者一个人花了一个半月到两个月时间写出来的，目标是一个前后端分离的网络论坛系统，目标基本实现了，但是又没实现，为什么这么说呢，以为本人以前没有去公司实习过，因此不懂企业级的产品的各种规范

### 技术框架
- 不得不说用的东西还是挺多的，虽然不一定说都特别熟悉这些东西，但是都是自己写的，还是比较了解，下面列举一下
- Spring-Boot 2.3.7  不用说都懂
- Mybatis Plus 3.4.2 也不用说，都懂
- Redis 6.2.0 当缓存和中间件
- Kafka 2.7.0 消息模块存用户还没接收的消息
- Zookeeper 3.7.0 Kafka依赖Zk，用来没干啥
- Vue3 大家也懂
- ElasticSearch 7.12.0 搜索模块高亮搜索
- Nginx 1.20.0 反向代理和负载均衡
- Mysql 8.0.13
- AliyunOSS 静态服务器
- Spring-Security 5.3.6 授权认证
- Canal 1.1.5 同步Mysql数据到es
- Spring-Websocket 5.2.12 服务器向用户推送消息，也是消息模块核心

### 部分功能展示
- 登陆
  ![登陆](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/login.png)
- 注册
  ![注册](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/signup.png)
- 重置密码
  ![重置密码](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/reset.png)
- 首页
  ![首页](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/main.png)
- 分区
  ![分区](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/zone.png)
- 帖子
  ![帖子](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/post.png)
- 个人资料
  ![个人资料](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/userdata.png)
- 聊天
  ![聊天](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/message.png)
- 管理
  ![管理](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/manage.png)
- 搜索
  ![管理](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/search.png)


### 技术架构图和功能模块
#### 就看看就行，以后服务肯定还要继续划分，主要模块都没用消息队列，原因是一开始感觉用不到，其实老实说是忘记有这个东西了，后面想加都写的差不多了不想改了
- LoneybearForum 主要模块
  ![架构](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/LoneybearFourm.png)
- LoneyebarForum 消息模块
  ![架构](https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/LoneybearForyum_Message.png)

### 功能模块图


### 有哪些问题
- 我提到了这个是一个先行体验版，所以我基本是展示一下，我以后这个项目一定会重构的，前后端到架构都会重构，到时候会把分好的各个服务发出来，现在服务就划分了消息和其他两个部分，前端也没单独分出来
- 页面加载没做loading看起来不太爽，然后Vue加载会显示一下加载之前的内容，影响观感
- 不是响应式布局，页面可能会与实物不符合
- 模块划分不够细，主要是因为懒，加上只有一个人，一个多月
- 服务器都是一个机器跑的，我自己macbook跑起来压力很大
- 禁用了缓存，为了方便测试
- 权限部分问题比较大，整个要重构，针对单个分区的用户处理可能有问题
- Redis当缓存或者当个中间表可能会更好
- 搜索模块就基本的搜索，当时没多少时间了
- 权限和安全这一块不太行，第一次用security还是自学的，以后会认真改
- 帖子内容发表情存在数据库里的是没有转义的，所以显示不出来
- 其他问题还有很多，就如前面提到的，只能算一个demo，作为毕业设计已经绰绰有余
- 你们真要跑估计跑不起来，环境难配置


