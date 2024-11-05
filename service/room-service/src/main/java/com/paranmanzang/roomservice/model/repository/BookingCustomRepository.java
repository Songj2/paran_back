package com.paranmanzang.roomservice.model.repository;

import com.paranmanzang.roomservice.model.domain.BookingTempModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingCustomRepository {
    Page<BookingTempModel> findByGroupId(Long id, Pageable pageable);
    Page<BookingTempModel> findByRoomId( Long id, Pageable pageable);

    Page<BookingTempModel> findEnabledByGroupId(Long groupId, Pageable pageable);
    Page<BookingTempModel> findDisabledByGroupId(Long groupId, Pageable pageable);
    Page<BookingTempModel> findPaidByGroupId(Long groupId, Pageable pageable);

    Page<BookingTempModel> findEnabledByRoomIds(List<Long> roomIds, Pageable pageable);
    Page<BookingTempModel> findDisabledByRoomIds(List<Long> roomIds, Pageable pageable);
    Page<BookingTempModel> findPaidByRoomIds(List<Long> roomIds, Pageable pageable);
}
