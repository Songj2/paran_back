package com.paranmanzang.roomservice.service.impl;

import com.paranmanzang.roomservice.model.domain.AccountModel;
import com.paranmanzang.roomservice.model.domain.BookingModel;
import com.paranmanzang.roomservice.model.domain.BookingTempModel;
import com.paranmanzang.roomservice.model.entity.Booking;
import com.paranmanzang.roomservice.model.entity.Room;
import com.paranmanzang.roomservice.model.repository.BookingRepository;
import com.paranmanzang.roomservice.model.repository.RoomRepository;
import com.paranmanzang.roomservice.service.BookingService;
import com.paranmanzang.roomservice.util.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final TimeServiceImpl timeService;
    private final AccountServiceImpl accountService;
    private final Converter converter;
    private final BookingModel bookingModel;

    @Override
    public BookingModel insert(BookingModel model) {
        return Optional.of(bookingRepository.save(Booking.builder()
                        .date(model.getDate())
                        .groupId(model.getGroupId())
                        .createAt(LocalDateTime.now())
                        .room(roomRepository.findById(model.getRoomId()).get())
                        .build()))
                .map(booking -> {
                    model.setId(booking.getId());
                    timeService.saveBooking(model);
                    return booking;
                }).map(converter::convertToBookingModel).map(bookingModel -> {
                    if (bookingModel.getUsingTime().isEmpty())
                        bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    return bookingModel;
                }).get();
    }

    @Override
    public Boolean delete(Long id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    BookingModel bookingModel = converter.convertToBookingModel(booking);
                    bookingRepository.delete(booking);
                    return timeService.saveBooking(bookingModel) && accountService.cancel(id);
                })
                .orElse(false);
    }

    @Override
    public Page<?> findByGroup(long groupId, Pageable pageable) {
        return bookingRepository.findByGroupId(groupId, pageable)
                .map(tempModel -> {
                    var bookingModel = tempToModel(tempModel);
                    bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    bookingModel.setAccount(accountService.findByBookingId(bookingModel.getId()));
                    return bookingModel;
                });
    }


    @Override
    public Page<?> findByRoom(long roomId, Pageable pageable) {
        return bookingRepository.findByRoomId(roomId, pageable)
                .map(tempModel -> {
                    var bookingModel = tempToModel(tempModel);
                    bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    bookingModel.setAccount(accountService.findByBookingId(bookingModel.getId()));
                    return bookingModel;
                });

    }

    @Override
    public Page<?> findEnabledByGroups(Long groupId, Pageable pageable) {
        return bookingRepository.findEnabledByGroupId(groupId, pageable)
                .map(tempModel -> {
                    var bookingModel = tempToModel(tempModel);
                    bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    bookingModel.setAccount(AccountModel.builder().build());
                    return bookingModel;
                });
    }

    @Override
    public Page<?> findDisabledByGroups(Long groupId, Pageable pageable) {
        return bookingRepository.findDisabledByGroupId(groupId, pageable)
                .map(tempModel -> {
                    var bookingModel = tempToModel(tempModel);
                    bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    bookingModel.setAccount(AccountModel.builder().build());
                    return bookingModel;
                });
    }

    @Override
    public Page<?> findPaidByGroups(Long groupId, Pageable pageable) {
        return bookingRepository.findPaidByGroupId(groupId, pageable)
                .map(tempModel -> {
                    var bookingModel = tempToModel(tempModel);
                    bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    bookingModel.setAccount(accountService.findByBookingId(bookingModel.getId()));
                    return bookingModel;
                });
    }

    @Override
    public Page<?> findEnabledByRooms(String nickname, Pageable pageable) {
        return bookingRepository.findEnabledByRoomIds(
                        roomRepository.findAllByNickname(nickname).stream().map(Room::getId).toList(), pageable
                )
                .map(tempModel -> {
                    var bookingModel = tempToModel(tempModel);
                    bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    bookingModel.setAccount(AccountModel.builder().build());
                    return bookingModel;
                });

    }

    @Override
    public Page<?> findPaidByRooms(String nickname, Pageable pageable) {
        return bookingRepository.findPaidByRoomIds(
                        roomRepository.findAllByNickname(nickname).stream().map(Room::getId).toList(), pageable)
                .map(tempModel -> {
                    var bookingModel = tempToModel(tempModel);
                    bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
                    bookingModel.setAccount(accountService.findByBookingId(bookingModel.getId()));
                    return bookingModel;
                });

    }

    @Override
    public Page<?> findDisabledByRooms(String nickname, Pageable pageable) {
        return bookingRepository.findDisabledByRoomIds(
                roomRepository.findAllByNickname(nickname).stream().map(Room::getId).toList(), pageable
        ).map(tempModel -> {
            var bookingModel = tempToModel(tempModel);
            bookingModel.setUsingTime(timeService.findByBooking(bookingModel.getId()));
            bookingModel.setAccount(accountService.findByBookingId(bookingModel.getId()));
            return bookingModel;
        });
    }


    @Override
    public BookingModel updateState(Long id) {
        if (bookingRepository.findById(id).get().isEnabled()) return null;

        return bookingRepository.findById(id).map(booking -> {
                    booking.setEnabled(true);
                    booking.setResponseAt(LocalDateTime.now());
                    return bookingRepository.save(booking);
                })
                .map(converter::convertToBookingModel).get();
    }

    @Override
    public BookingModel findById(Long id) {
        return converter.convertToBookingModel(bookingRepository.findById(id).get());
    }

    public BookingModel tempToModel(BookingTempModel tempModel) {
        return BookingModel.builder()
                .id(tempModel.getId())
                .enabled(tempModel.isEnabled())
                .date(tempModel.getDate())
                .roomId(tempModel.getRoomId())
                .groupId(tempModel.getGroupId())
                .roomName(tempModel.getRoomName())
                .address(tempModel.getAddress())
                .build();
    }

}
