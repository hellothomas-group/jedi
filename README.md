# Jedi(绝地) - a reliable TPM(thread pool management) system

[![Build Status](https://github.com/hellothomas-group/jedi/workflows/build/badge.svg)](https://github.com/hellothomas-group/jedi/actions)
[![Maven Central](https://img.shields.io/maven-central/v/xyz.hellothomas/jedi-client?color=blue)](https://mvnrepository.com/artifact/xyz.hellothomas/jedi-client)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Screenshots

线程池概览

![线程池概览](https://images.gitee.com/uploads/images/2021/0905/170437_924750df_5057838.png)

线程池实时状态监控

![线程池实时状态监控](https://images.gitee.com/uploads/images/2021/0905/170551_292e2bc5_5057838.png)

动态配置线程池参数

![动态配置线程池参数](https://images.gitee.com/uploads/images/2021/0905/170010_6be8758e_5057838.png)

线程池任务监控

![线程池任务汇总监控](https://images.gitee.com/uploads/images/2021/0905/170714_e8371566_5057838.png)

![线程池任务明细监控](https://images.gitee.com/uploads/images/2021/0905/170748_df7d6765_5057838.png)

线程池监控报警对接公司IM系统

![线程池监控报警对接公司IM系统](https://images.gitee.com/uploads/images/2021/0812/075117_1327fb49_5057838.jpeg)

# Features

- ## 实时展示线程池状态

- ## 线程池指标异常报警

- ## 线程池参数动态调整

- ## 线程池及任务数据分析

# Usage

- 服务端演示地址

  http://jedi.hellothomas.xyz:8080
  
  username: admin
  password: 123456

- Java客户端使用指南

  [客户端使用指南](https://github.com/hellothomas-group/jedi/wiki#%E4%B8%80-%E5%AE%A2%E6%88%B7%E7%AB%AF%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97)

# Design

[设计与实现](https://github.com/hellothomas-group/jedi/wiki#%E4%BA%8C-%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0)

# Development

# Deployment

[服务端部署指南](https://github.com/hellothomas-group/jedi/wiki#%E4%B8%89-%E6%9C%8D%E5%8A%A1%E7%AB%AF%E9%83%A8%E7%BD%B2%E6%8C%87%E5%8D%97)

# Release Notes

first release(0.0.10) will come soon

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