package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.entity.Booking;
import com.paranmanzang.roomservice.model.entity.QBooking;
import com.paranmanzang.roomservice.model.repository.BookingCustomRepository;
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
    private final QBooking booking= QBooking.booking;


    @Override
    public Page<Booking> findByGroupId(Long id, Pageable pageable) {
        var result= jpaQueryFactory.selectFrom(booking)
                .where(booking.id.in(
                        jpaQueryFactory.select(booking.id)
                                .from(booking)
                                .where(booking.groupId.eq(id))
                                .orderBy(booking.createAt.desc())
                                .limit( pageable.getPageSize())
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
        return new PageImpl<>( result, pageable, totalCount);
    }

    @Override
    public Page<Booking> findByRoomId(Long id, Pageable pageable) {
        var result= jpaQueryFactory.selectFrom(booking)
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
        return new PageImpl<>( result, pageable,totalCount);
    }

    @Override
    public Page<Booking> findEnabledByGroupIds(List<Long> groupIds, Pageable pageable) {
        var result= jpaQueryFactory.selectFrom(booking)
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
        return new PageImpl<>( result, pageable, totalCount);
    }

    @Override
    public Page<Booking> findDisabledByGroupIds(List<Long> groupIds, Pageable pageable) {
        var result= jpaQueryFactory.selectFrom(booking)
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
        return new PageImpl<>( result, pageable, totalCount );
    }

    @Override
    public Page<Booking> findEnabledByRoomIds(List<Long> roomIds, Pageable pageable) {
        var result= jpaQueryFactory.selectFrom(booking)
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
        return new PageImpl<>( result, pageable, totalCount);
    }

    @Override
    public Page<Booking> findDisabledByRoomIds(List<Long> roomIds, Pageable pageable) {
        var result= jpaQueryFactory.selectFrom(booking)
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
        return new PageImpl<>( result, pageable,totalCount);
    }
}
