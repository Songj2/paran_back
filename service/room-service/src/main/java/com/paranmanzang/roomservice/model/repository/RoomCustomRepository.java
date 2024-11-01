package com.paranmanzang.roomservice.model.repository;


import com.paranmanzang.roomservice.model.domain.RoomModel;
import com.paranmanzang.roomservice.model.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomCustomRepository {
    Page<RoomModel> findByEnabled(Pageable pageable);
    Page<RoomModel> findByDisabled(Pageable pageable);
    List<Room> findAllByNickname(String nickname);
    Page<?> findEnabledByNickname(String nickname, Pageable pageable);
    Page<?> findDisabledByNickname( String nickname, Pageable pageable);
}
