package com.paranmanzang.roomservice.util;

import com.paranmanzang.roomservice.model.domain.*;
import com.paranmanzang.roomservice.model.entity.*;
import com.paranmanzang.roomservice.model.repository.AccountRepository;
import com.paranmanzang.roomservice.model.repository.AddressRepository;
import com.paranmanzang.roomservice.model.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Converter {
    private final AccountRepository accountRepository;
    private final AddressRepository addressRepository;

    public AccountModel convertToAccountModel(Account account){
        return AccountModel.builder()
                .orderId(account.getOrderId())
                .orderName(account.getDetail())
                .amount(account.getAmount())
                .amountTaxFree(account.getAmountTaxFree())
                .groupId(account.getGroupId())
                .canceled(account.isCanceled())
                .reason(account.getReason())
                .createAt(account.getCreateAt())
                .roomId(account.getRoomId())
                .bookingId(account.getBookingId())
                .build();
    }
    public AddressModel convertTonAddressModel(Address address){
        return AddressModel.builder()
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .longitude(address.getLongitude())
                .latitude(address.getLatitude())
                .id(address.getId())
                .roomId(address.getRoomId())
                .build();
    }
    public BookingModel convertToBookingModel(Booking booking){
        return BookingModel.builder()
                .id(booking.getId())
                .date(booking.getDate())
                .enabled(booking.isEnabled())
                .usingTime(
                        Optional.ofNullable(booking.getTimes())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Time::getTime)
                        .toList())
                .roomId(booking.getRoom().getId())
                .groupId(booking.getGroupId())
                .roomName(booking.getRoom().getName())
                .address(addressRepository.findByRoomId(booking.getRoom().getId()).getAddress())
                .account((convertToAccountModel(accountRepository.findAccountByBookingId(booking.getId()).orElse(Account.builder().build()))))
                .build();
    }

    public ReviewModel convertToReviewModel(Review review){
        return ReviewModel.builder()
                .id(review.getId())
                .bookingId(review.getBooking().getId())
                .content(review.getContent())
                .createAt(review.getCreateAt())
                .nickname(review.getNickname())
                .rating(review.getRating())
                .roomId(review.getRoom().getId())
                .build();
    }

    public RoomModel convertToRoomModel(Room room){
        return RoomModel.builder()
                .name(room.getName())
                .closeTime(room.getCloseTime())
                .price(room.getPrice())
                .createdAt(room.getCreatedAt())
                .enabled(room.isEnabled())
                .id(room.getId())
                .maxPeople(room.getMaxPeople())
                .nickname(room.getNickname())
                .opened(room.isOpened())
                .openTime(room.getOpenTime())
                .build();
    }
    public TimeModel convertToTimeModel(Time time){
        return TimeModel.builder()
                .time(time.getTime().toString())
                .date(time.getDate().toString())
                .id(time.getId())
                .build();
    }
}
