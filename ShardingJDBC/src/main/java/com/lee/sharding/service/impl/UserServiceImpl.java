package com.lee.sharding.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.sharding.entity.User;
import com.lee.sharding.mapper.UserMapping;
import com.lee.sharding.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapping,User> implements UserService {
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
