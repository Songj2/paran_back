package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.domain.BookResponseModel;

import java.util.List;

public interface LikeBooksRepositoryCustom {
    List<BookResponseModel> findLikeBooksByNickname(String nickname);

}
