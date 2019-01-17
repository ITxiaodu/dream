package com.du.www.service;

import com.du.www.entity.Comment;

import java.util.List;

public interface CommentService {
    int add(Comment comment);

    void update(Comment comment);

    List<Comment> findAll(Long content_id);

    Comment findById(Long id);

    List<Comment> findAllFirstComment(Long content_id);

    List<Comment> findAllChildrenComment(Long content_id,String children);

    void deleteById(Long id);

    void deleteChildrenComment(String children);

}
