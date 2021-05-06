#####


<div style="display: flex;align-items: center">
    <img src="https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/images/run.gif" style="width: 50px;height: 50px">
    <a class="logo_link" href="#logo" style="font-family: Copperplate;src:url(https://loneybear.oss-cn-shanghai.aliyuncs.com/LoneybearForum/github/fonts/Copperplate.ttc);font-size: 30px;color: goldenrod">LoneybearForum</a>
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


### 有哪些问题
- 我提到了这个是一个先行体验版，所以我基本是展示一下，我以后这个项目一定会重构的，前后端到架构都会重构，到时候会把分好的各个服务发出来，现在服务就划分了消息和其他两个部分，前端也没单独分出来
- 页面加载没做loading看起来不太爽
- 你们真要跑估计跑不起来，环境难配置


