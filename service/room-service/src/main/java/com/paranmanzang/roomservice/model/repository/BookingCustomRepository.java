package com.paranmanzang.roomservice.model.repository;

import com.paranmanzang.roomservice.model.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingCustomRepository {
    Page<Booking> findByGroupId(Long id, Pageable pageable);
    Page<Booking> findByRoomId( Long id, Pageable pageable);

    Page<Booking> findEnabledByGroupIds(List<Long> groupIds, Pageable pageable);
    Page<Booking> findDisabledByGroupIds(List<Long> groupIds, Pageable pageable);

    Page<Booking> findEnabledByRoomIds(List<Long> roomIds, Pageable pageable);
    Page<Booking> findDisabledByRoomIds(List<Long> roomIds, Pageable pageable);
}
