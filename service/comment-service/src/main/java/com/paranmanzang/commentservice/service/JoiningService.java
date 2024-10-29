package com.paranmanzang.commentservice.service;

import com.paranmanzang.commentservice.model.domain.JoiningModel;

public interface JoiningService {
    Object addMember(JoiningModel joiningModel);

    Object enableMember(Long groupId, String userNickname);

    Object getUserListById(Long groupId);

    Object deleteUser(String nickname, Long groupId);

}
