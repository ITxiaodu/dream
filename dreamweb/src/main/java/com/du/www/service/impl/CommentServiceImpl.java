package com.du.www.service.impl;

import com.du.www.dao.CommentMapper;
import com.du.www.entity.Comment;
import com.du.www.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int add(Comment comment) {
        return commentMapper.insertComment(comment);
    }

    @Override
    public void update(Comment comment) {
        commentMapper.updateByPrimaryKey(comment);
    }

    @Override
    public List<Comment> findAll(Long cid) {
        return commentMapper.selectAll(cid);
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        return commentMapper.selectOne(comment);
    }

    @Override
    public List<Comment> findAllFirstComment(Long cid) {
        return commentMapper.findAllFirstComment(cid);
    }

    @Override
    public List<Comment> findAllChildrenComment(Long cid, String children) {
        return commentMapper.findAllChildrenComment(cid,children);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        commentMapper.deleteByPrimaryKey(comment);
    }

    @Override
    public void deleteChildrenComment(String children) {
        Example example = new Example(Comment.class);
        Example.Criteria criteria = example.createCriteria();
        List<Object> list = new ArrayList<Object>();
        String[] split = children.split(",");
        for (int i=0;i<split.length;i++){
            list.add(split[i]);
        }
        criteria.andIn("id",list);
        commentMapper.deleteByExample(example);
    }

    @Override
    public void deleteByContentId(Long cid) {
        Comment comment = new Comment();
        comment.setConId(cid);
        commentMapper.delete(comment);
    }
}
