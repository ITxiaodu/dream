package com.du.www.service.impl;


import com.du.www.dao.RoleMapper;
import com.du.www.entity.Role;
import com.du.www.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role findById(long id) {
        Role role = new Role();
        role.setId(id);
        return roleMapper.selectOne(role);
    }

    @Override
    public int add(Role role) {
       return roleMapper.insert(role);
    }
}
