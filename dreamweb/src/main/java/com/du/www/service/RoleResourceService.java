package com.du.www.service;

import com.du.www.entity.RoleResource;

import java.util.List;

public interface RoleResourceService {
    void add(RoleResource roleResource);

    RoleResource findById(Long id);

    List<RoleResource> findByRoleId(Long rid);

    void deleteById(Long id);
}
