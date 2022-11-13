package com.lee.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.sharding.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    Integer addUser(User user);

    List<User> selectLikeName(String name);
    Integer updateUser(User user);
}
