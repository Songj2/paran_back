package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.DeclarationPostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DeclarationPostService {
    Object insert(DeclarationPostModel declarationPostModel);
    boolean remove(Long id);
    Page<DeclarationPostModel> findAll(Pageable pageable);
    Page<DeclarationPostModel> findAllByNickname(String nickname, Pageable pageable);
    //Page<AdminPostModel> findAll(Pageable pageable);
    Object findByPostId(Long postId);

}
