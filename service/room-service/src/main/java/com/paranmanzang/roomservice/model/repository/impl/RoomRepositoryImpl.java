package com.paranmanzang.roomservice.model.repository.impl;

import com.paranmanzang.roomservice.model.domain.RoomModel;
import com.paranmanzang.roomservice.model.entity.QRoom;
import com.paranmanzang.roomservice.model.entity.Room;
import com.paranmanzang.roomservice.model.repository.RoomCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QRoom room = QRoom.room;

    @Override
    public Page<RoomModel> findByEnabled(Pageable pageable) {
        var result = jpaQueryFactory.select(
                        Projections.constructor(
                                RoomModel.class,
                                room.id.as("id"),
                                room.name.as("name"),
                                room.maxPeople.as("maxPeople"),
                                room.price.as("price"),
                                room.opened.as("opened"),
                                room.openTime.as("openTime"),
                                room.closeTime.as("closeTime"),
                                room.createdAt.as("createdAt"),
                                room.enabled.as("enabled"),
                                room.nickname.as("nickname")
                        )
                )
                .from(room)
                .where(room.id.in(
                        jpaQueryFactory.select(room.id)
                                .from(room)
                                .orderBy(room.id.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .where(room.enabled.eq(true))
                                .fetch()
                ))
                .orderBy(room.id.desc())
                .fetch().stream().toList();
        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(room.id.count())
                .from(room)
                .where(room.enabled.eq(true))
                .orderBy(room.id.desc())
                .fetchOne()).orElse(0L);

        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<RoomModel> findByDisabled(Pageable pageable) {
        var result = jpaQueryFactory.select(
                        Projections.constructor(
                                RoomModel.class,
                                room.id.as("id"),
                                room.name.as("name"),
                                room.maxPeople.as("maxPeople"),
                                room.price.as("price"),
                                room.opened.as("opened"),
                                room.openTime.as("openTime"),
                                room.closeTime.as("closeTime"),
                                room.createdAt.as("createdAt"),
                                room.enabled.as("enabled"),
                                room.nickname.as("nickname")
                        )
                )
                .from(room)
                .where(room.id.in(
                        jpaQueryFactory.select(room.id)
                                .from(room)
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset())
                                .where(room.enabled.eq(false))
                                .fetch()
                ))
                .fetch().stream().toList();

        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(room.id.count())
                .from(room)
                .where(room.enabled.eq(false))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public List<Room> findAllByNickname(String nickname) {
        return jpaQueryFactory.selectFrom(room)
                .where(room.nickname.eq(nickname))
                .fetch();
    }

    @Override
    public Page<?> findEnabledByNickname(String nickname, Pageable pageable) {
        var result = jpaQueryFactory.select(
                        Projections.constructor(
                                RoomModel.class,
                                room.id.as("id"),
                                room.name.as("name"),
                                room.maxPeople.as("maxPeople"),
                                room.price.as("price"),
                                room.opened.as("opened"),
                                room.openTime.as("openTime"),
                                room.closeTime.as("closeTime"),
                                room.createdAt.as("createdAt"),
                                room.enabled.as("enabled"),
                                room.nickname.as("nickname")
                        )
                )
                .from(room)
                .where(room.id.in(
                                jpaQueryFactory.select(room.id).from(room)
                                        .where(room.nickname.eq(nickname).and(room.enabled.eq(true)))
                                        .limit(pageable.getPageSize())
                                        .offset(pageable.getOffset())
                                        .fetch()
                        )
                )
                .fetch();

        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(room.id.count())
                .from(room)
                .where(room.nickname.eq(nickname).and(room.enabled.eq(true)))
                .fetchOne()).orElse(0L);
        log.info("샐러가 조회한다. enabled: {}, result={}", totalCount, result);
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<RoomModel> findDisabledByNickname(String nickname, Pageable pageable) {
        var result = jpaQueryFactory.select(
                        Projections.constructor(
                                RoomModel.class,
                                room.id.as("id"),
                                room.name.as("name"),
                                room.maxPeople.as("maxPeople"),
                                room.price.as("price"),
                                room.opened.as("opened"),
                                room.openTime.as("openTime"),
                                room.closeTime.as("closeTime"),
                                room.createdAt.as("createdAt"),
                                room.enabled.as("enabled"),
                                room.nickname.as("nickname")
                        )
                )
                .from(room)
                .where(room.id.in(
                                jpaQueryFactory.select(room.id).from(room)
                                        .where(room.nickname.eq(nickname).and(room.enabled.eq(false)))
                                        .limit(pageable.getPageSize())
                                        .offset(pageable.getOffset())
                                        .fetch()
                        )
                )
                .fetch();

        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(room.id.count())
                .from(room)
                .where(room.nickname.eq(nickname).and(room.enabled.eq(false)))
                .fetchOne()).orElse(0L);
        return new PageImpl<>(result, pageable, totalCount);
    }
}
