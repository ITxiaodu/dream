package com.du.www.service.impl;

import com.du.www.dao.UpvoteMapper;
import com.du.www.entity.Upvote;
import com.du.www.service.UpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpvoteServiceImpl implements UpvoteService {
    @Autowired
    private UpvoteMapper upvoteMapper;

    @Override
    public Upvote findByUidAndConId(Upvote upvote) {
        return upvoteMapper.selectOne(upvote);
    }

    @Override
    public int add(Upvote upvote) {
       return upvoteMapper.insert(upvote);
    }


    @Override
    public Upvote getByUid(Upvote upvote) {
        return null;
    }

    @Override
    public void update(Upvote upvote) {
        upvoteMapper.updateByPrimaryKey(upvote);
    }
}
