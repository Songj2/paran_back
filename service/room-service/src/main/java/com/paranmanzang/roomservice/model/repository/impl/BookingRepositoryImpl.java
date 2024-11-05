package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.domain.BookingModel;
import com.paranmanzang.roomservice.model.entity.Booking;
import com.paranmanzang.roomservice.model.entity.QAddress;
import com.paranmanzang.roomservice.model.entity.QBooking;
import com.paranmanzang.roomservice.model.entity.QRoom;
import com.paranmanzang.roomservice.model.repository.BookingCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QBooking booking = QBooking.booking;
    private final QAddress address = QAddress.address1;
    private final QRoom room= QRoom.room;

    @Override
    public Page<BookingModel> findByGroupId(Long id, Pageable pageable) {
        var result = jpaQueryFactory.selectDistinct(
                        Projections.constructor(
                                BookingModel.class,
                                booking.id.as("id"),
                                booking.enabled.as("enabled"),
                                booking.date.as("date"),
                                booking.room.id.as("roomId"),
                                booking.groupId.as("groupId"),
                                room.name.as("roomName"),
                                address.address.as("address")

                        )
                ).from(booking)
                .innerJoin(booking.room, room)
                .innerJoin(address).on(booking.room.id.eq(address.roomId))
                .where(booking.id.in(
                        jpaQueryFactory.select(booking.id)
                                .from(booking)
                                .where(booking.groupId.eq(id))
                                .orderBy(booking.createAt.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .fetch()
                ))
                .orderBy(booking.createAt.desc())
                .fetch();
        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(booking.id.count())
                .from(booking)
                .where(booking.groupId.eq(id))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<BookingModel> findByRoomId(Long id, Pageable pageable) {
        var result = jpaQueryFactory.selectDistinct(
                        Projections.constructor(
                                BookingModel.class,
                                booking.id.as("id"),
                                booking.enabled.as("enabled"),
                                booking.date.as("date"),
                                booking.room.id.as("roomId"),
                                booking.groupId.as("groupId"),
                                room.name.as("roomName"),
                                address.address.as("address")

                        )
                ).from(booking)
                .innerJoin(booking.room, room)
                .innerJoin(address).on(booking.room.id.eq(address.roomId))
                .where(booking.id.in(
                        jpaQueryFactory.select(booking.id)
                                .from(booking)
                                .where(booking.room.id.eq(id))
                                .orderBy(booking.createAt.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .fetch()
                ))
                .orderBy(booking.createAt.desc())
                .fetch();
        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(booking.id.count())
                .from(booking)
                .where(booking.room.id.eq(id))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<BookingModel> findEnabledByGroupIds(List<Long> groupIds, Pageable pageable) {
        var result = jpaQueryFactory.selectDistinct(
                        Projections.constructor(
                                BookingModel.class,
                                booking.id.as("id"),
                                booking.enabled.as("enabled"),
                                booking.date.as("date"),
                                booking.room.id.as("roomId"),
                                booking.groupId.as("groupId"),
                                room.name.as("roomName"),
                                address.address.as("address")

                        )
                ).from(booking)
                .innerJoin(booking.room, room)
                .innerJoin(address).on(booking.room.id.eq(address.roomId))
                .where(booking.id.in(
                        jpaQueryFactory.select(booking.id)
                                .from(booking)
                                .where(booking.groupId.in(groupIds).and(booking.enabled.eq(true)))
                                .orderBy(booking.createAt.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .fetch()
                ))
                .orderBy(booking.createAt.desc())
                .fetch();
        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(booking.id.count())
                .from(booking)
                .where(booking.groupId.in(groupIds).and(booking.enabled.eq(true)))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<BookingModel> findDisabledByGroupIds(List<Long> groupIds, Pageable pageable) {
        var result = jpaQueryFactory.selectDistinct(
                        Projections.constructor(
                                BookingModel.class,
                                booking.id.as("id"),
                                booking.enabled.as("enabled"),
                                booking.date.as("date"),
                                booking.room.id.as("roomId"),
                                booking.groupId.as("groupId"),
                                room.name.as("roomName"),
                                address.address.as("address")

                        )
                ).from(booking)
                .innerJoin(booking.room, room)
                .innerJoin(address).on(booking.room.id.eq(address.roomId))
                .where(booking.id.in(
                        jpaQueryFactory.select(booking.id)
                                .from(booking)
                                .where(booking.groupId.in(groupIds).and(booking.enabled.eq(false)))
                                .orderBy(booking.createAt.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .fetch()
                ))
                .orderBy(booking.createAt.desc())
                .fetch();
        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(booking.id.count())
                .from(booking)
                .where(booking.groupId.in(groupIds).and(booking.enabled.eq(false)))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<BookingModel> findEnabledByRoomIds(List<Long> roomIds, Pageable pageable) {
        var result = jpaQueryFactory.selectDistinct(
                        Projections.constructor(
                                BookingModel.class,
                                booking.id.as("id"),
                                booking.enabled.as("enabled"),
                                booking.date.as("date"),
                                booking.room.id.as("roomId"),
                                booking.groupId.as("groupId"),
                                room.name.as("roomName"),
                                address.address.as("address")

                        )
                ).from(booking)
                .innerJoin(booking.room, room)
                .innerJoin(address).on(booking.room.id.eq(address.roomId))
                .where(booking.id.in(
                        jpaQueryFactory.select(booking.id)
                                .from(booking)
                                .where(booking.room.id.in(roomIds).and(booking.enabled.eq(true)))
                                .orderBy(booking.createAt.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .fetch()
                ))
                .orderBy(booking.createAt.desc())

                .fetch();
        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(booking.id.count())
                .from(booking)
                .where(booking.room.id.in(roomIds).and(booking.enabled.eq(true)))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<BookingModel> findDisabledByRoomIds(List<Long> roomIds, Pageable pageable) {
        var result = jpaQueryFactory.selectDistinct(
                        Projections.constructor(
                                BookingModel.class,
                                booking.id.as("id"),
                                booking.enabled.as("enabled"),
                                booking.date.as("date"),
                                booking.room.id.as("roomId"),
                                booking.groupId.as("groupId"),
                                room.name.as("roomName"),
                                address.address.as("address")

                        )
                ).from(booking)
                .innerJoin(booking.room, room)
                .innerJoin(address).on(booking.room.id.eq(address.roomId))
                .where(booking.id.in(
                        jpaQueryFactory.select(booking.id)
                                .from(booking)
                                .where(booking.room.id.in(roomIds).and(booking.enabled.eq(false)))
                                .orderBy(booking.createAt.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .fetch()
                ))
                .orderBy(booking.createAt.desc())
                .fetch();
        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(booking.id.count())
                .from(booking)
                .where(booking.room.id.in(roomIds).and(booking.enabled.eq(false)))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }
}
