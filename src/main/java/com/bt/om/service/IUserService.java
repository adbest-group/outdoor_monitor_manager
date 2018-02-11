package com.bt.om.service;

import com.bt.om.entity.User;

public interface IUserService {
    public User getByMobile(String mobile);
}
