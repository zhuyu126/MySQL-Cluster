# 双主一从高可用集群
双主一从的搭建跟之前的一主多从类似，需要注意的是双主之间需要配置为互为主从关系

```mysql
change master to
master_host='192.168.58.8',
master_user='root',
master_password='zy12260011',
MASTER_AUTO_POSITION=1;

change master to
master_host='192.168.58.9',
master_user='root',
master_password='zy12260011',
MASTER_AUTO_POSITION=1;
```

同时从节点也需要配置两个Master主节点的数据源

```mysql
change master to
master_host='192.168.58.8',
master_user='root',
master_password='zy12260011',
MASTER_AUTO_POSITION=1
FOR CHANNEL 'm1';

change master to
master_host='192.168.58.9',
master_user='root',
master_password='zy12260011',
MASTER_AUTO_POSITION=1
FOR CHANNEL 'm2';
```

## keepalived

Keepalived 下载并配置

```bash
# 下载keepalive
yum install keepalived -y
# 配置keepalived
vi /etc/keepalived/keepalived.conf
# 配置通知的email
global_defs {
   notification_email {
     acassen@firewall.loc
     failover@firewall.loc
     sysadmin@firewall.loc
   }
   notification_email_from Alexandre.Cassen@firewall.loc
   smtp_server 192.168.58.8
   smtp_connect_timeout 30
   router_id LVS_DEVEL
   vrrp_skip_check_adv_addr
   vrrp_strict
   vrrp_garp_interval 0
   vrrp_gna_interval 0
}
# 检查mysql脚本，定时执行
vrrp_script check_run {
   script "/usr/local/check_run.sh"
   interval 3
}
# 设置虚拟ip
vrrp_instance VI_1 {
    # 当前节点的状态MASTER、BACKUP
    state MASTER
    # 当前服务器使用的网卡名称，使用ifconfig查看
    interface eth0
    #VRRP组名，两个节点的设置必须一样
    virtual_router_id 51
    #Master节点的优先级（1-254之间）
    priority 100
    #组播信息发送间隔，两个节点设置必须一样
    advert_int 1
    #设置验证信息，两个节点必须一致
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    #虚拟IP,对外提供MySQL服务的IP地址
    virtual_ipaddress {
        192.168.58.206
    }
}
```

### 检查脚本check_run.sh

```bash
#!/bin/bash
. /root/.bashrc
count=1

while true
do

mysql -uroot -pitxiongge@1 -S /var/lib/mysql/mysql.sock -e "select now();" > /dev/null 2>&1
i=$?
ps aux | grep mysqld | grep -v grep > /dev/null 2>&1
j=$?
if [ $i = 0 ] && [ $j = 0 ]
then
   exit 0
else
   if [ $i = 1 ] && [ $j = 0 ]
   then
       exit 0
   else
        if [ $count -gt 5 ]
        then
              break
        fi
   let count++
   continue
   fi
fi

done

systemctl stop keepalived.service
```

### 启动keepalived

```bash
systemctl start keepalived
```

## Mycat

### 下载Mycat

```bash
wget http://dl.mycat.org.cn/1.6.7.1/Mycat-server-1.6.7.1-release-20190627191042-linux.tar.gz tar -zxf Mycat-server-1.6.7.1-release-20190627191042-linux.tar.gz
```

### 修改配置文件

```bash
vi mycat/conf/server.xml
<user name="root" defaultAccount="true">
    <property name="password">zy12260011</property>
    <property name="schemas">hello</property>
</user>
vi mycat/conf/server.xml
<schema name="hello" checkSQLschema="true" sqlMaxLimit="100">
      <table name="t" dataNode="dn1"/>
</schema>
<dataNode name="dn1" dataHost="host1" database="hello" />
<dataHost name="host1" maxCon="1000" minCon="10" balance="1" writeType="1" dbType="mysql" dbDriver="native" switchType="1" slaveThreshold="100">
      <heartbeat>select user()</heartbeat>
      <writeHost host="hostM1" url="192.168.58.8:3306" user="root" password="zy12260011">
            <readHost host="hostS1" url="192.168.58.10:3306" user="root" password="zy12260011" />
      </writeHost>
      <writeHost host="hostM2" url="192.168.58.9:3306" user="root" password="zy12260011" />
</dataHost>
```



