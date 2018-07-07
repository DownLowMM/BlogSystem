<p align="center">
	<img width="130px" src="https://raw.githubusercontent.com/DuanJiaNing/Pictures/master/BlogSystem/logo.png"/>
	<br/><h1 align="center">BlogSystem<br/></h1><br/><br/>
</p>

## 博客系统

BLOG 是学习 JavaWeb 开发4个月以来的总结实践项目，使用 SSM（Spring、SpringMVC、MyBatis）框架，MVC 三层结构、Lucene全文检索引擎、Junit 4单元测试、logback日志框架、Druid数据库连接池、Shiro安全框架的一个博文系统；

在线查看：[sample](http://120.79.128.250:8080/)<br>

#### 项目简介

网站面向有撰写博客习惯的用户，个人可注册成为网站用户（博主），在系统中创建自己的博文类别、标签，使用Markdown语法创作博文，创作好后将博文分类，贴上标签既可发布；普通用户通过用户名就能浏览和检索博主的公开博文，注册成为博主后可以评论、喜欢和收藏博文。

### 在 dev/2.0.0 分支把项目的源代码结构修改为多模块方式

部署方式

1. 导入 sql ：blog-config/resources/sql/db_blog.sql
2. 修改数据库连接密码：blog-config/resources/config/db.properties
3. 如果电脑上没有 E 盘（Windows 系统），修改 blog-config/resources/config/conf.properties 中一些文件保存路径
4. 配置项目
5. 部署运行，参看，主页（注册页）底部左侧指南

#### 配置项目说明
![](https://raw.githubusercontent.com/DuanJiaNing/Pictures/master/BlogSystem/dep-1.png)
<br>
![](https://raw.githubusercontent.com/DuanJiaNing/Pictures/master/BlogSystem/dep-2.png)


License
============

    Copyright 2017 DuanJiaNing

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.


