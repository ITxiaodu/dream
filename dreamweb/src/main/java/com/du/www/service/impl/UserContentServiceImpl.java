package com.du.www.service.impl;

import com.du.www.common.PageHelper;
import com.du.www.dao.CommentMapper;
import com.du.www.dao.UserContentMapper;
import com.du.www.entity.Comment;
import com.du.www.entity.UserContent;
import com.du.www.service.UserContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserContentServiceImpl implements UserContentService{
    @Autowired
    private UserContentMapper userContentMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageHelper.Page<UserContent> findAll(UserContent content, Integer pageNum, Integer pageSize) {
        System.out.println("第"+pageNum+"页");
        System.out.println("每页显示："+pageSize+"条");
        PageHelper.startPage(pageNum,pageSize);
        List<UserContent> list = userContentMapper.select(content);
        PageHelper.Page endPage = PageHelper.endPage();
        List<UserContent> result = endPage.getResult();
        return endPage;
    }

    @Override
    public PageHelper.Page<UserContent> findAll(UserContent content, Comment comment, Integer pageNum, Integer pageSize) {
        System.out.println("第"+pageNum+"页");
        System.out.println("每页显示"+pageSize+"条");
        PageHelper.startPage(pageNum,pageSize);
        List<UserContent> list = userContentMapper.select(content);
        List<Comment> comments = commentMapper.select(comment);
        PageHelper.Page endPage = PageHelper.endPage();
        List<UserContent> result = endPage.getResult();
        return endPage;
    }

    @Override
    public PageHelper.Page<UserContent> findAllByUpvote(UserContent content, Integer pageNum, Integer pageSize) {
        Example e = new Example(UserContent.class);
        e.setOrderByClause("upvote DESC");
        PageHelper.startPage(pageNum,pageSize);
        List<UserContent> list = userContentMapper.selectByExample(e);
        PageHelper.Page endPage = PageHelper.endPage();
        return endPage;
    }

    @Override
    public void addContent(UserContent content) {
        userContentMapper.insert(content);
    }

    @Override
    public List<UserContent> findByUserId(Long uid) {
        UserContent userContent =new UserContent();
        userContent.setuId(uid);
        List<UserContent> list =userContentMapper.select(userContent);
        return list;
    }

    @Override
    public List<UserContent> findAll() {
        return userContentMapper.select(null);
    }

    @Override
    public UserContent findById(long id) {
        UserContent userContent = new UserContent();
        userContent.setId(id);
        return userContentMapper.selectOne(userContent);
    }

    @Override
    public void updateById(UserContent content) {
        userContentMapper.updateByPrimaryKeySelective(content);
    }
}
