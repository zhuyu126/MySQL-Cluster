package com.lee.mycat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lee.mycat.entity.User;
import com.lee.mycat.mapper.UserMapping;
import com.lee.mycat.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapping, User> implements UserService {
    @Override
    public Integer addUser(User user) {
        return baseMapper.insert(user);
    }

    @Override
    public List<User> selectLikeName(String name) {
        name =  "%" + name + "%";
        return baseMapper.selectLikeName(name);
    }

    @Override
    public Integer updateUser(User user) {
        return baseMapper.updateById(user);
    }


}
