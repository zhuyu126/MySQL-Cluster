# 一主多从集群搭建
## 1.MySQL安装
MySQL安装为基础能力不做赘述
## 2. 修改Master的my.cnf配置
```bash
   # binlog刷盘策略
   sync_binlog=1
   # 需要备份的数据库
   binlog-do-db=hello
   # 不需要备份的数据库
   binlog-ignore-db=mysql
   # 启动二进制文件
   log-bin=mysql-bin
   # 服务器ID
   server-id=1
```

重启Mysql服务。

## 3. 主机Master给从机Slave授权备份

```mysql
mysql>GRANT REPLICATION SLAVE ON *.* TO 'root'@'192.168.58.5' identified by 'zy12260011';
```

## 4.刷新权限并查询Master状态

```mysql
mysql> FLUSH PRIVILEGES;
# 查询master状态
mysql> show master status;
```

![master状态](http://image.crystallee.top/master%E7%8A%B6%E6%80%81.png)

## 5.修改从服务器my.cnf

```bash
server-id=2
```

## 6.重启mysql服务并配置Slave

```mysql
systemctl restart mysql
mysql>change master to
master_host='192.168.58.4',
master_port=3306,
master_user='root',
master_password='zy12260011',
master_log_file='mysql-bin.000009',
master_log_pos=19820,
MASTER_AUTO_POSITION=0;
```

## 7. 启动从服务器复制功能并检查复制功能状态

```mysql
mysql>start slave;
mysql> show slave status;
```

![Slave库复制状态](http://image.crystallee.top/Slave%E5%BA%93%E5%A4%8D%E5%88%B6%E7%8A%B6%E6%80%81.png)

⚠️剩余两个从库操作如上

## 8.Spring Boot+ShardingJDBC+MybatisPlus的CRUD操作

### 8.1 添加操作

![一主多从数据添加](http://image.crystallee.top/%E4%B8%80%E4%B8%BB%E5%A4%9A%E4%BB%8E%E6%95%B0%E6%8D%AE%E6%B7%BB%E5%8A%A0.png)

### 8.2根据ID查询

![一主多从ID查询](http://image.crystallee.top/%E4%B8%80%E4%B8%BB%E5%A4%9A%E4%BB%8EID%E6%9F%A5%E8%AF%A2.png)

### 8.3 数据更新

![一主多从更新](http://image.crystallee.top/%E4%B8%80%E4%B8%BB%E5%A4%9A%E4%BB%8E%E6%9B%B4%E6%96%B0.png)

### 8.4 数据删除

![一主多从删除](http://image.crystallee.top/%E4%B8%80%E4%B8%BB%E5%A4%9A%E4%BB%8E%E5%88%A0%E9%99%A4.png)