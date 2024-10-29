package com.paranmanzang.commentservice.service;

import com.paranmanzang.commentservice.model.domain.GroupPostModel;
import com.paranmanzang.commentservice.model.domain.GroupPostResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupPostService {

    Page<GroupPostResponseModel> findByGroupId(Long groupId, Pageable pageable, String postCategory);

    GroupPostResponseModel savePost(GroupPostModel groupPostModel);

    Object updateViewCount(Long postId);
}
