# company-frame

#### 介绍
基于spring boot 2.1.6、shiro、jwt、redis、swagger2、mybatis 、thymeleaf、layui 后台管理系统， 权限控制的方式为 RBAC。代码通熟易懂 、JWT（无状态token）过期自动刷新，数据全程 ajax 获取，封装 ajax 工具类、菜单无线层级展示，解决 layui.tree 树形组件，回显问题。数据交互都是以 JSON 格式交互。后台接口RESTful 风格，支持前后端分离，app公用一套接口。 

#### 软件架构
软件架构说明
* 核心框架：spring boot 2.1.6
* 持久层框架：mybatis
* 数据库连接池：alibaba druid
* 安全框架：apache shiro
* 无状态 JWT
* 缓存框架：redis(自定义 RedisTemplate 序列化)
* 日志框架：logback
* 接口文档：swagger 2.9.2
* 前端模板：thymeleaf+layui2x

#### **部署**

- 下载redis 启动redis
- 创建company_frame数据库
- 导入company_frame.sql
- 启动项目
- 接口文档访问 http://localhost:8080/swagger-ui.html#/
- 登录地址 http://localhost:8080/index/login
- 登录密码 都是 666666


####  还提供了配套视频讲解
##### 手机扫码观看
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/16.jpg) 

[网易云课堂获取视频](https://study.163.com/course/introduction.htm?share=2&shareId=480000002172852&courseId=1209674835&_trace_c_p_k2_=69ae6b3d440249759ad085487f89f73a)

![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/15.jpg) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/13.jpg) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/14.jpg)

#### 效果展示

![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/1.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/2.JPG)
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/4.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/5.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/6.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/7.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/8.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/9.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/10.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/11.JPG) 
![](https://raw.githubusercontent.com/huo785/company-frame/master/src/main/resources/static/images/class/12.JPG)  




