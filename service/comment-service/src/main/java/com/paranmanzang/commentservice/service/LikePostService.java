package com.paranmanzang.commentservice.service;


import com.paranmanzang.commentservice.model.domain.GroupPostResponseModel;
import com.paranmanzang.commentservice.model.domain.LikePostModel;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface LikePostService {
    //좋아요
    Object insert(LikePostModel likePostModel);

    //좋아요 취소
    boolean remove(LikePostModel likePostModel);

    //마이페이지에서 조회
    List<GroupPostResponseModel> findAllByNickname(String nickname);

}
