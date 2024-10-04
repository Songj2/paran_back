package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.AdminPostModel;
import com.paranmanzang.userservice.model.entity.AdminPosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminPostService {
    //게시글 작성
    Object createAPost(AdminPostModel adminPostModel);
    //게시글 업데이트
    Object updateAPost(Long id, AdminPostModel adminPostModel);
    //게시글 삭제
    boolean deleteAPost(Long id);
    //내가 쓴 게시글 확인
    Page<AdminPostModel> getMyAPost(String nickname, Pageable pageable);
    //게시판 들어가면 뜨는거
    Page<AdminPostModel> getAPost(Pageable pageable);
    //게시글 상세조회
    AdminPosts getAdminPostsById(Long id);
    //조회수 확인
    Long getViewCount(Long id);
}


