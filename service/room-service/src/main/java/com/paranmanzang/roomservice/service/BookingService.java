package com.paranmanzang.roomservice.service;

import com.paranmanzang.roomservice.model.domain.BookingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {
    BookingModel insert(BookingModel model);
    BookingModel updateState(Long id);
    BookingModel findById(Long id);
    Boolean delete(Long id);

    Page<?> findByGroup(long groupId, Pageable pageable);

    Page<?> findByRoom(long roomId, Pageable pageable);

    Page<?> findEnabledByGroups(Long groupId, Pageable pageable);

    Page<?> findDisabledByGroups(Long groupId, Pageable pageable);

    Page<?> findPaidByGroups(Long groupId, Pageable pageable);

    Page<?> findEnabledByRooms(Long id, Pageable pageable);

    Page<?> findDisabledByRooms(Long id, Pageable pageable);

    Page<?> findPaidByRooms(Long id, Pageable pageable);
}
