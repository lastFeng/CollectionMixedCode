### SpringCloud 基于Dalston版本

#### SpringCloud简介
Spring Cloud是一个基于Spring Boot实现的云应用开发工具，它为基于JVM的云应用开发中涉及的配置管理、服务发现、断路器、智能路由、微代理、控制总线、全局锁、决策竞选、分布式会话和集群状态管理等操作提供了一种简单的开发方式。

#### SpringCloud模块介绍

##### 服务治理模块

由于Spring Cloud为服务治理做了一层抽象接口，所以在Spring Cloud应用中可以支持多种不同的服务治理框架，比如：Netflix Eureka、Consul、Zookeeper。在Spring Cloud服务治理抽象层的作用下，我们可以无缝地切换服务治理实现，并且不影响任何其他的服务注册、服务发现、服务调用等逻辑。

###### Spring Cloud Eureka
Spring Cloud Eureka是Spring Cloud Netflix项目下的服务治理模块。而Spring Cloud Netflix项目是Spring Cloud的子项目之一，主要内容是对Netflix公司一系列开源产品的包装，它为Spring Boot应用提供了自配置的Netflix OSS整合。通过一些简单的注解，开发者就可以快速的在应用中配置一下常用模块并构建庞大的分布式系统。它主要提供的模块包括：服务发现（Eureka），断路器（Hystrix），智能路由（Zuul），客户端负载均衡（Ribbon）等。


服务注册中心：


通过@EnableEurekaServer注解启动一个服务注册中心提供给其他应用进行对话。这一步非常的简单，只需要在一个普通的Spring Boot应用中添加这个注解就能开启此功能


在默认设置下，该服务注册中心也会将自己作为客户端来尝试注册它自己，所以我们需要禁用它的客户端注册行为，只需要在application.properties配置文件中增加如下信息：
```properties
# eureka-server服务名称
spring.application.name=eureka-server
# 端口
server.port=1001

# 地址
eureka.instance.hostname=localhost
# 不允许自注册
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```


服务提供方：

添加注解：@EnableDiscoveryClient

设置配置文件：
```
spring.application.name=eureka-client
server.port=2001
# 查找Eureka服务
eureka.client.serviceUrl.defaultZone=http://localhost:1001/eureka/
```

###### Spring Cloud Consul
Spring Cloud Consul项目是针对Consul的服务治理实现。Consul是一个分布式高可用的系统，它包含多个组件，但是作为一个整体，在微服务架构中为我们的基础设施提供服务发现和服务配置的工具。它包含了下面几个特性：
   - 服务发现
   - 健康检查
   - Key/Value存储
   - 多数据中心

##### 服务消费
###### LoadBalancerClient
通过该接口来进行服务的消费--对应模块为eureka-comsumer

###### Spring Cloud Ribbon
Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端负载均衡的工具。它是一个基于HTTP和TCP的客户端负载均衡器。它可以通过在客户端中配置ribbonServerList来设置服务端列表去轮询访问以达到均衡负载的作用。





