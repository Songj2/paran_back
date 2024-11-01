package com.paranmanzang.commentservice.model.repository;

import com.paranmanzang.commentservice.model.entity.Joining;

public interface JoiningRepositoryCustom {
    Joining findByNicknameAndGroupId(String nickname, Long groupId);
}