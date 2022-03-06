# Jedi(绝地) - a reliable TPM(thread pool management) system

[![Build Status](https://github.com/hellothomas-group/jedi/workflows/build/badge.svg)](https://github.com/hellothomas-group/jedi/actions)
[![Maven Central](https://img.shields.io/maven-central/v/xyz.hellothomas/jedi-client?color=blue)](https://mvnrepository.com/artifact/xyz.hellothomas/jedi-client)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Screenshots

线程池实时状态监控

![线程池实时状态监控](https://images.gitee.com/uploads/images/2022/0207/230949_7fbeb3de_5057838.png)

动态维护线程池配置

![动态配置线程池参数](https://images.gitee.com/uploads/images/2022/0207/231106_46c28a98_5057838.png)

线程池任务监控

![线程池任务监控](https://images.gitee.com/uploads/images/2022/0207/231125_51aa1ea5_5057838.png)

线程池任务重试

![线程池任务重试](https://images.gitee.com/uploads/images/2022/0207/231142_8e3b2cfe_5057838.png)

# Features

- ### 实时监控异步调度（线程池和异步任务），故障预警

- ### 动态维护线程池配置，及时规避故障或加速故障恢复

- ### 异步调度任务高可靠，支持任务持久化

- ### 异步调度任务高可用，支持异步任务自恢复和重试补偿

- ### 易扩展，业务系统可自行实现接口自定义功能

- ### 易用，低侵入（只需注解，无需编码）

# Usage

- 平台演示地址

  http://jedi.hellothomas.xyz:8080

  username: admin
  password: 123456

  <mark>请勿对jedi-demo示例应用做非查询操作</mark>

- 客户端使用指南

  [SDK使用指南](https://github.com/hellothomas-group/jedi/wiki#%E4%B8%80-%E5%AE%A2%E6%88%B7%E7%AB%AF%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97)
  
  客户端演示地址，服务器最低配置，各位浅尝辄止
  
  http://jedi-demo.hellothomas.xyz:8090/swagger-ui.html

# Design

**功能设计**

![功能设计图](https://user-images.githubusercontent.com/30972648/156911422-afb26a3f-a7e3-4d44-8099-c7a95f7170ab.png)

**系统架构**

![系统架构图](https://user-images.githubusercontent.com/30972648/156911221-973307c3-afe4-4e6b-8786-44760b4f4b61.png)

# Development

1.  下载最新源码。
2. 数据库建表 doc/db目录下tables_jedi_config.sql、tables_jedi_consumer.sql。
3. 调整jedi-config、jedi-consumer、jedi-admin模块中resources/application-local.yml的数据库配置，分别运行三个后端应用。
4. 启动前端应用，cd jedi-ui，已安装npm，则执行npm run start。未安装则执行npm install。访问 http://localhost:8089登录，用户名/密码 admin/123456。
5. 启动示例应用jedi-demo。数据库建表，db/init.sql。调整resources/application-local.yml的数据库配置，运行应用。访问http://127.0.0.1:8090/swagger-ui.html触发请求。

# Deployment

[平台部署指南](https://github.com/hellothomas-group/jedi/wiki#%E4%B8%89-%E6%9C%8D%E5%8A%A1%E7%AB%AF%E9%83%A8%E7%BD%B2%E6%8C%87%E5%8D%97)

# Release Notes

0.0.11 - first release version

# FAQ

# License

[Apache 2 license](https://github.com/hellothomas-group/jedi/blob/main/LICENSE).

# Known Users

![招商银行](http://www.cmbchina.com/Images/header/app/icon_zsyh.png)招商银行

# Stars
Gitee Stars
[![Stargazers over time](https://whnb.wang/stars/hellothomas/jedi)](https://www.whnb.wang/hellothomas/jedi)

Github Stars
[![Stargazers over time](https://starchart.cc/hellothomas-group/jedi.svg)](https://starchart.cc/hellothomas-group/jedi)

# Thanks
线程池监控思路参考 [Java线程池实现原理及其在美团业务中的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)

动态配置部分参考 [Apollo Config](https://github.com/ctripcorp/apollo)