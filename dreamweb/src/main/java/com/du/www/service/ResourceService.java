package com.du.www.service;

import com.du.www.entity.Resource;

import java.util.List;

public interface ResourceService {
    void add(Resource resource);

    Resource findById(Long id);

    List<Resource> findAll();

    void deleteById(Long id);

    void update(Resource resource);
}
