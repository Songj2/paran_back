package com.paranmanzang.commentservice.model.repository;

import com.paranmanzang.commentservice.model.domain.GroupPostResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupPostRepositoryCustom {
    Page<GroupPostResponseModel> findGroupPostsByGroupId(Long groupId, Pageable pageable, String postCategory);

    GroupPostResponseModel findByPostId(Long postId);
}
