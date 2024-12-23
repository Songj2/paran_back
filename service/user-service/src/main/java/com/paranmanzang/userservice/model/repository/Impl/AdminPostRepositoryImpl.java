package com.paranmanzang.userservice.model.repository.Impl;


import com.paranmanzang.userservice.model.domain.AdminPostModel;
import com.paranmanzang.userservice.model.entity.QAdminPosts;
import com.paranmanzang.userservice.model.repository.custom.AdminPostRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AdminPostRepositoryImpl implements AdminPostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QAdminPosts adminPosts = QAdminPosts.adminPosts;

    @Override
    public Page<AdminPostModel> findAdminPostByNickname(String nickname, Pageable pageable) {
        var adminPostIdsN = jpaQueryFactory
                .select(adminPosts.id)
                .from(adminPosts)
                .where(adminPosts.nickname.eq(nickname))
                .orderBy(adminPosts.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AdminPostModel> adminPostModelsn = adminPostIdsN.isEmpty() ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                AdminPostModel.class,
                                adminPosts.id,
                                adminPosts.nickname,
                                adminPosts.category,
                                adminPosts.title,
                                adminPosts.content,
                                adminPosts.createAt,
                                adminPosts.viewCount,
                                adminPosts.modifyAt
                        ))
                        .from(adminPosts)
                        .where(adminPosts.id.in(adminPostIdsN))
                        .orderBy(adminPosts.id.desc())
                        .fetch();

        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(adminPosts.id.count())
                .from(adminPosts)
                .where(adminPosts.nickname.eq(nickname))
                .orderBy(adminPosts.createAt.desc())
                .fetchOne()).orElse(0L);

        return new PageImpl<>(adminPostModelsn, pageable, totalCount);
    }

    @Override
    public Page<AdminPostModel> findAllPost(Pageable pageable) {
        var adminPostIds = jpaQueryFactory
                .select(adminPosts.id)
                .from(adminPosts)
                .orderBy(adminPosts.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<AdminPostModel> adminPostModel = adminPostIds.isEmpty() ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                AdminPostModel.class,
                                adminPosts.id,
                                adminPosts.nickname,
                                adminPosts.category,
                                adminPosts.title,
                                adminPosts.content,
                                adminPosts.createAt,
                                adminPosts.viewCount,
                                adminPosts.modifyAt
                        ))
                        .from(adminPosts)
                        .where(adminPosts.id.in(adminPostIds))
                        .orderBy(adminPosts.id.desc())
                        .fetch();

        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(adminPosts.id.count())
                .from(adminPosts)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(adminPostModel, pageable, totalCount);

    }

}
