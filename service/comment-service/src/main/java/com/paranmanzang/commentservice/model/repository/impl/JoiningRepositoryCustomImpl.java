package com.paranmanzang.commentservice.model.repository.impl;

import com.paranmanzang.commentservice.model.entity.Joining;
import com.paranmanzang.commentservice.model.repository.JoiningRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.paranmanzang.commentservice.model.entity.QJoining.joining;

@RequiredArgsConstructor
public class JoiningRepositoryCustomImpl implements JoiningRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Joining findByNicknameAndGroupId(String nickname, Long groupId) {
        queryFactory.selectFrom(joining)
                .where(joining.nickname.eq(nickname).and(joining.group.id.eq(groupId)))
                .fetchOne();
    }
}