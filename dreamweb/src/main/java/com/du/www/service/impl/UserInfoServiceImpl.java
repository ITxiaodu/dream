package com.du.www.service.impl;

import com.du.www.dao.UserInfoMapper;
import com.du.www.entity.UserInfo;
import com.du.www.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService{
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public UserInfo findByUid(Long id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        return userInfoMapper.selectOne(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKey(userInfo);
    }

    @Override
    public void add(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }
}
