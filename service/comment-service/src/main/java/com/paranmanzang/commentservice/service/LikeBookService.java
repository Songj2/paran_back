package com.paranmanzang.commentservice.service;

import com.paranmanzang.commentservice.model.domain.BookResponseModel;
import com.paranmanzang.commentservice.model.domain.LikeBookModel;
import org.springframework.validation.BindException;

import java.util.List;

public interface LikeBookService {
    //책 좋아요
    Object add(LikeBookModel likeBooKModel) throws BindException;

    //좋아요 취소
    Boolean remove(LikeBookModel likeBooKModel) throws BindException;

    //마이페이지 책 찜 조회
    List<BookResponseModel> findAllByNickname(String nickname);
}
