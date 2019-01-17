package com.du.www.service;

import com.du.www.entity.User;

public interface UserService {
    int regist(User user);
    User login(String email,String password);
    User findByEmail(String email);
    User findByphone(String phone);
    User findById(Long id);
    void deleteByEmail(String email);
    void update(User user);
}
