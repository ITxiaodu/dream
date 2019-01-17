package com.du.www.service.impl;

import com.du.www.dao.UserMapper;
import com.du.www.entity.User;
import com.du.www.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public int regist(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User login(String name, String password) {
        User user = new User();
        user.setEmail(name);
        user.setPassword(password);
        return userMapper.selectOne(user);
    }

    @Override
    public User findByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
    }

    @Override
    public User findByphone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return userMapper.selectOne(user);
    }

    @Override
    public User findById(Long id) {
        User user = new User();
        user.setId(id);
        return userMapper.selectOne(user);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        userMapper.delete(user);
    }

    @Override
    public void update(User user) {
       userMapper.updateByPrimaryKeySelective(user);
    }

    public void deleteByEmailAndFalse(String email){
        User user =new User();
        user.setEmail(email);
        userMapper.delete(user);
    }
}
