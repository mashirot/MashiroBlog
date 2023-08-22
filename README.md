# MashiroBlog

一套前后端分离的博客项目，后端使用Java，Kotlin混合开发，前端采用Vue+TypeScript

## 项目介绍

本项目是博客的后端，使用SpringBoot框架，持久层使用MybatisPlus框架，
配合SpringSecurity进行鉴权，使用Redis，RabbitMQ等中间件

## 部署

1. clone项目

   `git clone https://github.com/mashirot/MashiroBlog.git`

2. 配置

   配置`src/main/resources/application.yml`和`mail-service/src/main/resources/application.yml`

3. 数据库初始化，

    执行`blog.sql`

4. 打包

   `gradle build -x test`

5. 运行（JDK17+）

6. 设置管理员

   向后端`/admin/reg`接口发送POST请求，携带如下JSON请求体
   ```json
   {
   "username": "mashiro",
   "password": "123456",
   "nickname": "Shiina",
   "email": "ShiinaMashiro@sakurasou.com",
   "profile": "ねぇ，あなたは何色になりたい?"
   }
   ```