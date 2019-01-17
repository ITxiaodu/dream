package com.du.www.service.impl;

import com.du.www.dao.RoleUserMapper;
import com.du.www.entity.RoleUser;
import com.du.www.entity.User;
import com.du.www.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleUserServiceImpl implements RoleUserService{
    @Autowired
    private RoleUserMapper roleUserMapper;

    @Override
    public List<RoleUser> findByUser(User user) {
        RoleUser roleUser = new RoleUser();
        roleUser.setuId(user.getId());
        return roleUserMapper.select(roleUser);
    }

    @Override
    public int add(RoleUser roleUser) {
        return roleUserMapper.insert(roleUser);
    }

    @Override
    public void deleteByUid(Long uid) {
        RoleUser roleUser = new RoleUser();
        roleUser.setuId(uid);
        roleUserMapper.delete(roleUser);
    }
}
