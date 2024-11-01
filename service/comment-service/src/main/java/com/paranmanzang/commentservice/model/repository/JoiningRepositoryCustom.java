package com.paranmanzang.commentservice.model.repository;

public interface JoiningRepositoryCustom {
    void deleteByNicknameAndGroupId(String nickname, Long groupId);
}