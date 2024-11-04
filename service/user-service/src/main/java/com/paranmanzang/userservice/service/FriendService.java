package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.FriendModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FriendService {

    //친구 요청 전송
    Object insertRequest(FriendModel friend);

    //친구 추가, 친구 요청 승인
    Object update(FriendModel friendModel);

    //친구 삭제, 친구 요청 거절
    boolean remove(Long id);

    //친구 리스트 조회
    List<FriendModel> findAllByNickname(String nickname) ;

    //친구 요청 리스트 조회
    List<FriendModel> findAllRequestByNickname(String nickname);


}
