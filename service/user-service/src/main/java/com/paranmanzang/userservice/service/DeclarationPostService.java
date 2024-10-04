package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.DeclarationPostModel;
import com.paranmanzang.userservice.model.entity.DeclarationPosts;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public interface DeclarationPostService {
    Object createDPost(DeclarationPostModel declarationPostModel);
    boolean deleteDPost(Long id);
    Page<DeclarationPostModel> getDPostAdmin(Pageable pageable);
    Page<DeclarationPostModel> getDPost(String nickname, Pageable pageable);
    //Page<AdminPostModel> getAPost(Pageable pageable);
    Object getPostDetail(Long postId);

}
