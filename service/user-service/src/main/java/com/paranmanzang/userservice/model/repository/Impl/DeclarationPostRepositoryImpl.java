package com.paranmanzang.userservice.model.repository.Impl;


import com.paranmanzang.userservice.model.domain.DeclarationPostModel;
import com.paranmanzang.userservice.model.entity.QDeclarationPosts;
import com.paranmanzang.userservice.model.repository.custom.DeclarationPostRepositoryCustom;
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
public class DeclarationPostRepositoryImpl implements DeclarationPostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QDeclarationPosts declarationPosts = QDeclarationPosts.declarationPosts;

    // declarer에 따라 게시물을 찾는 메서드
    @Override
    public Page<DeclarationPostModel> findByNickname(String nickname, Pageable pageable) {
        log.info("Repository Impl) {}, {}", nickname, pageable);
        // DeclarationPosts의 ID만 선택하여 리스트로 저장
        List<Long> declarationpostsIds = jpaQueryFactory
                .select(declarationPosts.id)  // ID만 선택
                .from(declarationPosts)
                .where(declarationPosts.declarer.eq(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // ID 리스트가 비어 있지 않으면 데이터를 조회
        if (declarationpostsIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0L);
        }
        List<DeclarationPostModel> declarationPostModel =
                jpaQueryFactory
                        .select(Projections.constructor(
                                DeclarationPostModel.class,
                                declarationPosts.id,
                                declarationPosts.title,
                                declarationPosts.content,
                                declarationPosts.target,
                                declarationPosts.declarer,
                                declarationPosts.createdAt
                        ))
                        .from(declarationPosts)
                        .where(declarationPosts.id.in(declarationpostsIds))  // ID 리스트 사용
                        .fetch();

        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(declarationPosts.id.count())
                .from(declarationPosts)
                .where(declarationPosts.declarer.eq(nickname))
                .fetchOne()).orElse(0L);

        // 결과를 Page 객체로 반환
        return new PageImpl<>(declarationPostModel, pageable, totalCount);
    }

    // 모든 게시물을 찾는 메서드
    @Override
    public Page<DeclarationPostModel> findAllPost(Pageable pageable) {
        // DeclarationPosts의 ID만 선택하여 리스트로 저장
        List<Long> declarationpostsIds = jpaQueryFactory
                .select(declarationPosts.id)  // ID만 선택
                .from(declarationPosts)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // ID 리스트가 비어 있지 않으면 데이터를 조회
        List<DeclarationPostModel> declarationPostModelList = declarationpostsIds.isEmpty() ? List.of() :
                jpaQueryFactory
                        .select(Projections.constructor(
                                DeclarationPostModel.class,
                                declarationPosts.id,
                                declarationPosts.title,
                                declarationPosts.content,
                                declarationPosts.target,
                                declarationPosts.declarer,
                                declarationPosts.createdAt
                        ))
                        .from(declarationPosts)
                        .where(declarationPosts.id.in(declarationpostsIds))  // ID 리스트 사용
                        .fetch();

        long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(declarationPosts.id.count())
                .from(declarationPosts)
                .fetchOne()).orElse(0L);
        // 결과를 Page 객체로 반환
        return new PageImpl<>(declarationPostModelList, pageable, totalCount);
    }
}
