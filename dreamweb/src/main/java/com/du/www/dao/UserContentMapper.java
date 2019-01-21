package com.du.www.dao;

import com.du.www.entity.UserContent;
import org.springframework.data.repository.query.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserContentMapper extends Mapper<UserContent> {
    List<UserContent> findCategoryByUid(@Param("uid") long uid);
    List<UserContent> findByJoin(UserContent userContent);
    int insertContent(UserContent userContent);
}
