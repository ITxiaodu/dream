package com.du.www.service;

import com.du.www.entity.LoginLog;

import java.util.List;

public interface LoginLogService {
    int add(LoginLog loginLog);

    List<LoginLog> findAll();

    List<LoginLog> findByUid(Long uid);
}
