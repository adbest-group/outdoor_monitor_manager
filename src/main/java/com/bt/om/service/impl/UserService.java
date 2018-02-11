package com.bt.om.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.User;
import com.bt.om.mapper.UserMapper;
import com.bt.om.service.IUserService;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }
}
