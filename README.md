# Spring-Boot-Blog
# 版本
Elastic Search开始用的是2.2.4后面改成了5以后的版本。
Spring Boot在开始的时候是1.5并没使用2.0的新版本特性。所以如果换版本可能会存在仓库层的方法需要更换以及。一系列的东西需要更改。MogonDB file 已经在我的仓库中贴了出来
# 前端 
 使用Thymeleaf 提供spring标准方言和一个与 SpringMVC 完美集成的可选模块，可以快速的实现表单绑定、属性编辑器、国际化等功能。
 使用Bootstrap作为一个web框架,JQuery作为JS的框架
# 后端
主要是使用了 Spring的一些技术栈，包括Spring boot spring mvc 用来处理前端访问和后端的控制，把模型绑定到页面中通知做信息的返回。
使用spring Data JPA进行数据的封装和操作  
安全方面使用了 Spring security进行安全设置
通过Hibernate 里面的关系对象和映射来进行数据库实际语句的操作。通过Hibernate和JPA的配合使用，通过对象的方式来避免了编写Sql语句的繁琐的动作。
# 数据存储
使用了MySQL进行实体类的存储，博客信息和博客用户信息，标签信息，博客正文内容的存储
同时也使用了H2 进行数据快速的测试  
Mongo DB 实现图片的存储
文件搜索使用了ElasticSearch 
# 项目的构建
使用的是Gradle,简化Maven繁琐xml配置、强大支持多工程构建、Groove语言性能。
# Bug处理
如果无法点击进入博客页面或者发表博客之后显示500错误就请清除ES中、data目录下面的所有文件
# 项目运行截图
![主页](https://github.com/bedbanan/Spring-Boot-Blog/blob/master/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200629094954.png)
![主页](https://github.com/bedbanan/Spring-Boot-Blog/blob/master/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200629095010.png)
![主页](https://github.com/bedbanan/Spring-Boot-Blog/blob/master/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200629095013.png)
![主页](https://github.com/bedbanan/Spring-Boot-Blog/blob/master/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200629095021.png)
