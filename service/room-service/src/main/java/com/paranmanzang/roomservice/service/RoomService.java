package com.paranmanzang.roomservice.service;

import com.paranmanzang.roomservice.model.domain.RoomModel;
import com.paranmanzang.roomservice.model.domain.RoomUpdateModel;
import com.paranmanzang.roomservice.model.domain.RoomWTimeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    RoomModel insert(RoomModel model);

    RoomModel update(RoomUpdateModel model);

    Boolean delete(Long id);

    RoomModel confirm(Long id);
    Page<?> findByEnabled(Pageable pageable);
    List<?> getIdAllEnabled();
    RoomModel findById(Long id);
    List<?> findAllByEnabled();
    RoomWTimeModel findByIdWithTime(Long id);
    List<?> findByLikeRoom(String nickname);

    Page<?> findEnabledByNickname(String nickname, Pageable pageable);

    Page<?> findDisabledByNickname(String nickname, Pageable pageable);

    Page<?> findByDisabled(Pageable pageable);
}