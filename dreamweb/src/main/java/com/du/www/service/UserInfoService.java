package com.du.www.service;

import com.du.www.entity.UserInfo;

public interface UserInfoService {
    UserInfo findByUid(Long id);

    void update(UserInfo userInfo);

    void add(UserInfo userInfo);
}
