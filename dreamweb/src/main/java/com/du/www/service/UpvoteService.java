package com.du.www.service;

import com.du.www.entity.Upvote;

public interface UpvoteService {

    Upvote findByUidAndConId(Upvote upvote);

    int add(Upvote upvote);

    Upvote getByUid(Upvote upvote);

    void update(Upvote upvote);

    void deleteByContentId(Long cid);
}
