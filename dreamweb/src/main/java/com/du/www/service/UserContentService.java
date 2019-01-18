package com.du.www.service;

import com.du.www.common.PageHelper;
import com.du.www.entity.Comment;
import com.du.www.entity.UserContent;


import java.util.List;

public interface UserContentService {

    PageHelper.Page<UserContent> findAll(UserContent content,Integer pageNum,Integer pageSize);

    PageHelper.Page<UserContent> findAll(UserContent content, Comment comment,Integer pageNum,Integer pageSize);

    PageHelper.Page<UserContent> findAllByUpvote(UserContent content,Integer pageNum,Integer pageSize);


    int addContent(UserContent content);

    List<UserContent> findByUserId(Long uid);

    List<UserContent> findAll();

    UserContent findById(long id);

    void  updateById(UserContent content);

    List<UserContent> findCategoryByUid(Long uid);

    PageHelper.Page<UserContent> findPersonal(Long uid,Integer pageNum,Integer pageSize);

    PageHelper.Page<UserContent> findByCategory(String category,Long uid,Integer pageNum,Integer pageSize);

    void deleteById(Long cid);
}
