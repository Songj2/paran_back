package com.paranmanzang.userservice.model.repository.custom;

import com.paranmanzang.userservice.model.domain.DeclarationPostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface DeclarationPostRepositoryCustom {
    Page<DeclarationPostModel> findByNickname(String nickname, Pageable pageable);

    Page<DeclarationPostModel> findAllPost(Pageable pageable);
 }
