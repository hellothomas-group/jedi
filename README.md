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

- ### 动态维护线程池配置，降低维护成本，及时规避故障或加速故障恢复

- ### 异步调度任务高可靠，默认与数据库可靠性级别一致，可自行扩展提高级别

- ### 异步调度任务高可用，支持异步任务自恢复和重试，故障后快速补偿异常任务

- ### 易扩展，业务系统可自行实现接口，替换默认实现

- ### 易用，低侵入（只需注解，无需编码）

# Usage

- 服务端演示地址

  http://jedi.hellothomas.xyz:8080

  username: admin
  password: 123456

  <mark>请勿对jedi-demo示例应用做非查询操作</mark>

- Java客户端使用指南

  [客户端使用指南](https://github.com/hellothomas-group/jedi/wiki#%E4%B8%80-%E5%AE%A2%E6%88%B7%E7%AB%AF%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97)

# Design

[设计与实现](https://github.com/hellothomas-group/jedi/wiki#%E4%BA%8C-%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0)

# Development

# Deployment

[服务端部署指南](https://github.com/hellothomas-group/jedi/wiki#%E4%B8%89-%E6%9C%8D%E5%8A%A1%E7%AB%AF%E9%83%A8%E7%BD%B2%E6%8C%87%E5%8D%97)

# Release Notes

first release(1.0.0) will come soon

# FAQ

# License

[Apache 2 license](https://github.com/hellothomas-group/jedi/blob/main/LICENSE).

# Known Users

# Stars
Gitee Stars
[![Stargazers over time](https://whnb.wang/stars/hellothomas/jedi)](https://www.whnb.wang/hellothomas/jedi)

Github Stars
[![Stargazers over time](https://starchart.cc/hellothomas-group/jedi.svg)](https://starchart.cc/hellothomas-group/jedi)

# Thanks
线程池监控思路参考 [Java线程池实现原理及其在美团业务中的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)

动态配置部分参考 [Apollo Config](https://github.com/ctripcorp/apollo)