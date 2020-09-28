## 简介

本着『前后端分离，人不分离』的要领，开发了此基于 Spring Boot + Vue 前后端分离博客系统

计划编写详细博客教程（如果有人关注这个项目的话👀）



## 后端

1. 核心框架：Spring Boot
2. 安全框架：Spring Security
3. Token 认证：jjwt
4. 持久层框架：MyBatis
5. 分页插件：PageHelper
6. NoSQL缓存：Redis
7. Markdown 转 HTML：commonmark-java



基于 JDK8 开发，8以上要添加依赖：

```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
```



## 前端

核心框架：Vue、Vue Router、Vuex

Vue 项目基于 @vue/cli4.x 构建



### 后台界面

[Element UI](https://github.com/ElemeFE/element)：后台 CMS 部分完全基于 Element UI 开发



### 前台界面

[Semantic UI](https://semantic-ui.com/)：主要使用，页面布局样式，个人感觉挺好看的 UI 框架，比较适合前台界面的开发，语义化的 css，前一版博客系统使用过，可惜该框架 Vue 版的开发完成度不高，见 [Semantic UI Vue](https://semantic-ui-vue.github.io/#/)

[Element UI](https://github.com/ElemeFE/element)：部分使用，一些小组件，弥补了 Semantic UI 的不足，便于快速实现效果

MarkDown 排版：基于 [typo.css](https://github.com/sofish/typo.css) 修改



## And...

自用博客，长期维护，欢迎 issues, pr

