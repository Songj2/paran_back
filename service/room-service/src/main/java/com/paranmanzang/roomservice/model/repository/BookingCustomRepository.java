package com.paranmanzang.roomservice.model.repository;

import com.paranmanzang.roomservice.model.domain.BookingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingCustomRepository {
    Page<BookingModel> findByGroupId(Long id, Pageable pageable);
    Page<BookingModel> findByRoomId( Long id, Pageable pageable);

    Page<BookingModel> findEnabledByGroupId(Long groupId, Pageable pageable);
    Page<BookingModel> findDisabledByGroupId(Long groupId, Pageable pageable);
    Page<BookingModel> findPaidByGroupId(Long groupId, Pageable pageable);

    Page<BookingModel> findEnabledByRoomIds(List<Long> roomIds, Pageable pageable);
    Page<BookingModel> findDisabledByRoomIds(List<Long> roomIds, Pageable pageable);
    Page<BookingModel> findPaidByRoomIds(List<Long> roomIds, Pageable pageable);
}
