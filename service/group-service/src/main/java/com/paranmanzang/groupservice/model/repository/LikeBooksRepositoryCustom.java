package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.domain.LikeBookModel;

import java.util.List;

public interface LikeBooksRepositoryCustom {
    List<LikeBookModel> findLikeBooksByNickname(String nickname);

}
