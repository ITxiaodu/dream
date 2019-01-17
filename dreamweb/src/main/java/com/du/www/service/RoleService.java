package com.du.www.service;

import com.du.www.entity.Role;

public interface RoleService {
    Role findById(long id);

    int add(Role role);
}
