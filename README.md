# springBoot+vue+layui聊天室 非前后端分离

### 介绍
webSocket作为数据传输、mysql作为数据存储

### 软件架构
SpringBoot 2.4.13  、Mysql、thymeleaf....


### 安装教程


前后端不分离项目：
idea，
mysql，
（navicat或者mysqlworkbench），
maven (可选)

缺工具包但是又不想下载的话可以给个好评，联系我，我发全套安装包给你


1. 打开你的navicat 或者 mysqlworkbench， 先创建数据库，再导入数据库文件 （数据库文件夹内）
2. 打开你的idea，导入后台源码文件（有pom文件的是后台文件），开始加载依赖，如果依赖加载很慢可以配置阿里云的镜像，详情搜csdn即可
3. 依赖下载之配置applicaiton.yml文件的数据库配置或者redis配置
4. 启动你的后端项目

datasource:
username: root
password: 2013400236 （你的数据库密码）
url: jdbc:mysql://127.0.0.1:3306/chat?allowMultiQueries=true&characterEncoding=utf-8&useSSL=false
driver-class-name: com.mysql.cj.jdbc.Driver
#指定数据源
type: com.alibaba.druid.pool.DruidDataSource 



### 用户

​		123456、123457、123458、123459

​		密码均为：123456

#### 图片、文件存放

请把根目录”的 “chat.zip文件 ”移动到D盘下解压。

```yml
file:
	path: D:\chat  
```


