<p align="center">
	<img width="130px" src="https://raw.githubusercontent.com/DuanJiaNing/Pictures/master/BlogSystem/logo.png"/>
	<br/><h1 align="center">BlogSystem<br/></h1><br/><br/>
</p>

## 项目简介

网站面向有撰写博客习惯的用户，个人可注册成为网站用户（博主），在系统中创建自己的博文类别、标签，使用Markdown语法创作博文，创作好后将博文分类，贴上标签既可发布；普通用户通过用户名就能浏览和检索博主的公开博文，注册成为博主后可以评论、喜欢和收藏博文。

### 在 dev/2.0.0 分支把项目的源代码结构修改为多模块方式
- blogos-api

spring boot 项目，对外暴露 api 接口

- blogos-service

项目的所有服务接口定义，后续可业务拆分为更细的模块

- blogos-service-impl
blogos-service 接口的实现

- blogos-web-sample

spring + jsp 前端

部署方式
1. blogos-web-sample 直接以 jar 的方式依赖 blogos-service-impl，这导致 blogos-web-sample 项目中需要对
blogos-service-impl 中的组件（bean） 进行扫描路径配置，后续 blogos-service-impl 拆分为微服务就可以
去掉这一部分配置。

2. spring boot 模块启动在 7070 端口，blogos-web-sample 模块按 war 的方式启动即可

#### 现存问题
1. blogos-web-sample 中对图片的 url 访问直接在 .jsp 写死了路径，需要配置化
2. 还没完整的测试走通

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


