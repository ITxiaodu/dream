package com.du.www.service;

import com.du.www.entity.RoleUser;
import com.du.www.entity.User;

import java.util.List;

public interface RoleUserService {

    List<RoleUser> findByUser(User user);

    int add(RoleUser roleUser);

    void deleteByUid(Long uid);
}
