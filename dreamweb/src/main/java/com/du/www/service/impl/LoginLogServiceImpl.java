package com.du.www.service.impl;

import com.du.www.dao.LoginLogMapper;
import com.du.www.entity.LoginLog;
import com.du.www.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService{
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public int add(LoginLog loginLog) {
        return loginLogMapper.insert(loginLog);
    }

    @Override
    public List<LoginLog> findAll() {
       return  loginLogMapper.select(null);
    }

    @Override
    public List<LoginLog> findByUid(Long uid) {
        LoginLog loginLog = new LoginLog();
        loginLog.setuId(uid);
        return loginLogMapper.select(loginLog);
    }
}
