package com.du.www.service.impl;

import com.du.www.common.PageHelper;
import com.du.www.dao.CommentMapper;
import com.du.www.dao.UserContentMapper;
import com.du.www.entity.Comment;
import com.du.www.entity.UserContent;
import com.du.www.service.UserContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        List<UserContent> list = userContentMapper.findByJoin(content);
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
    public int addContent(UserContent content) {
         return  userContentMapper.insert(content);
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
        List<UserContent> list = userContentMapper.findByJoin(userContent);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void updateById(UserContent content) {
        userContentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public List<UserContent> findCategoryByUid(Long uid) {
        return userContentMapper.findCategoryByUid(uid);
    }

    @Override
    public PageHelper.Page<UserContent> findByCategory(String category, Long uid, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();
        if (StringUtils.isNoneBlank(category)&&!"null".equals(category)){
            userContent.setCategory(category);
        }
        userContent.setuId(uid);
        userContent.setPersonal("0");
        PageHelper.startPage(pageNum,pageSize);
        userContentMapper.select(userContent);
        PageHelper.Page endPage = PageHelper.endPage();
        return endPage;
    }

    @Override
    public PageHelper.Page<UserContent> findPersonal(Long uid, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();
        userContent.setuId(uid);
        userContent.setPersonal("1");
        PageHelper.startPage(pageNum,pageSize);
        userContentMapper.select(userContent);
        PageHelper.Page endPage = PageHelper.endPage();
        return endPage;
    }

    @Override
    public void deleteById(Long cid) {
        userContentMapper.deleteByPrimaryKey(cid);
    }

    @Override
    public PageHelper.Page<UserContent> findAll(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<UserContent> list = userContentMapper.findByJoin(null);
        PageHelper.Page endPage = PageHelper.endPage();
        return endPage;
    }

}
