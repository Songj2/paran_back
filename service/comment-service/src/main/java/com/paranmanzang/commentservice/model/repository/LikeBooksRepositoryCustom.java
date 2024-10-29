package com.paranmanzang.commentservice.model.repository;

import com.paranmanzang.commentservice.model.domain.BookResponseModel;

import java.util.List;

public interface LikeBooksRepositoryCustom {
    List<BookResponseModel> findLikeBooksByNickname(String nickname);

}
