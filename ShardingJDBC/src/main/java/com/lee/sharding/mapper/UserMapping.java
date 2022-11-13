package com.lee.sharding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.sharding.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserMapping extends BaseMapper<User> {
    List<User> selectLikeName(@Param("name") String name);
}