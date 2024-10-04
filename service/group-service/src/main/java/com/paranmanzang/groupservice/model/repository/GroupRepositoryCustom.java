package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.domain.GroupResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GroupRepositoryCustom {
    Page<GroupResponseModel> findGroup(Pageable pageable);

    Page<GroupResponseModel> findByNickname(String nickname, Pageable pageable);

    Page<GroupResponseModel> findByEnable(Pageable pageable);


}
