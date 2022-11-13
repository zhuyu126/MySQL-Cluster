package com.lee.sharding.test;

import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.lee.sharding.entity.User;
import com.lee.sharding.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    /**
     * 数据添加
     */
    @Test
    public void add(){
        for (int i=1;i<30;i++){
            User user=new User();
            user.setId(i);
            user.setAge(10+i);
            user.setName("name"+i);
            userService.addUser(user);
        }
    }

    /**
     * 查询所有
     */
    @Test
    public void findAll(){
        List<User> userList = userService.list();
        userList.forEach(System.out::println);
    }

    /**
     * 根据ID查询
     */
    @Test
    public void findById(){
        User user = userService.getById(1);
        System.out.println(user);
    }
    @Test
    public void update(){
        User user=new User();
        user.setId(1);
        user.setName("update test");
        userService.updateUser(user);
    }
    @Test
    public void delete(){
        boolean b = userService.removeById(1);
    }
}
