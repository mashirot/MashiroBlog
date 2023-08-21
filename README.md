# MashiroBlog

一套前后端分离的博客项目，后端使用Java，Kotlin混合开发，前端采用Vue+TypeScript

## 项目介绍

本项目是博客的后端，使用SpringBoot框架，持久层使用MybatisPlus框架，
配合SpringSecurity进行鉴权，使用Redis，RabbitMQ等中间件

## 部署

1. clone项目

    `git clone https://github.com/mashirot/MashiroBlog.git`

2. 配置

   1. 分别配置`src/main/resources/application.yml`和`mail-service/src/main/resources/application.yml`

   2. 修改`src/main/java/ski/mashiro/config/WebMvcConfig.java`中的前端地址，或用`*`代替（不推荐）
   
3. 数据库初始化，设置管理员
    
   1. 执行`blog.sql`
   2. 修改`src/test/java/ski/mashiro/MashiroBlogApplicationTests.java`中的`insertUser`方法内容，
   除id外修改为你的，并运行测试，通过即为成功，请不要添加超过**1**个用户

4. 打包
5. 运行（JDK17+）