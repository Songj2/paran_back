package com.paranmanzang.commentservice.model.repository;

import com.paranmanzang.commentservice.model.domain.GroupPostResponseModel;

import java.util.List;

public interface LikePostRepositoryCustom {
    List<GroupPostResponseModel> findLikePostByNickname(String nickname);
}
