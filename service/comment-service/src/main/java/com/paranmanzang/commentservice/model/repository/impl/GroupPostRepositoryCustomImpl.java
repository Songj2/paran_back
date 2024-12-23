package com.paranmanzang.commentservice.model.repository.impl;

import com.paranmanzang.commentservice.model.domain.GroupPostResponseModel;
import com.paranmanzang.commentservice.model.repository.GroupPostRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.paranmanzang.commentservice.model.entity.QBook.book;
import static com.paranmanzang.commentservice.model.entity.QGroup.group;
import static com.paranmanzang.commentservice.model.entity.QGroupPost.groupPost;

@Slf4j
@RequiredArgsConstructor
public class GroupPostRepositoryCustomImpl implements GroupPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GroupPostResponseModel> findGroupPostsByGroupId(Long groupId, Pageable pageable, String postCategory) {
        var ids = queryFactory
                .select(groupPost.id)
                .from(groupPost)
                .where(
                        groupPost.group.id.eq(groupId)
                                .and(groupPost.postCategory.eq(postCategory))
                )
                .orderBy(groupPost.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.info("카테고리: {}, ids:{}", postCategory, ids);

        // Step 2: 필요한 필드 조회 및 GroupPostResponseModel 변환
        List<GroupPostResponseModel> content = ids.isEmpty() ? List.of() :
                queryFactory
                        .selectDistinct(Projections.constructor(
                                GroupPostResponseModel.class,
                                groupPost.id.as("id"),
                                groupPost.title.as("title"),
                                groupPost.content.as("content"),
                                groupPost.createAt.as("createAt"),
                                groupPost.modifyAt.as("modifyAt"),
                                groupPost.postCategory.as("postCategory"),
                                groupPost.viewCount.as("viewCount"),
                                groupPost.nickname.as("nickname"),
                                groupPost.group.id.as("groupId"),
                                groupPost.group.name.as("groupName"),
                                groupPost.book.id.as("bookId"),
                                groupPost.book.title.as("bookTitle")
                        ))
                        .from(groupPost)
                        .leftJoin(groupPost.group, group)  // left join to handle null groups
                        .leftJoin(groupPost.book, book)    // left join to handle null books
                        .where(groupPost.id.in(ids))
                        .orderBy(groupPost.id.desc())
                        .fetch();

        long totalCount = Optional.ofNullable(queryFactory
                .select(groupPost.id.count())
                .from(groupPost)
                .where(
                        groupPost.group.id.eq(groupId)
                                .and(groupPost.postCategory.eq(postCategory))
                )
                .fetchOne()).orElse(0L);
        log.info("소모임 게시글: id:{}, pageable:{}, content:{}", groupId, pageable, content);
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public GroupPostResponseModel findByPostId(Long postId) {
        return queryFactory
                .selectDistinct(Projections.constructor(
                        GroupPostResponseModel.class,
                        groupPost.id.as("id"),
                        groupPost.title.as("title"),
                        groupPost.content.as("content"),
                        groupPost.createAt.as("createAt"),
                        groupPost.modifyAt.as("modifyAt"),
                        groupPost.postCategory.as("postCategory"),
                        groupPost.viewCount.as("viewCount"),
                        groupPost.nickname.as("nickname"),
                        groupPost.group.id.as("groupId"),
                        groupPost.group.name.as("groupName"),
                        groupPost.book.id.as("bookId"),
                        groupPost.book.title.as("bookTitle")
                ))
                .from(groupPost)
                .leftJoin(groupPost.group, group)
                .leftJoin(groupPost.book, book)
                .where(groupPost.id.eq(postId))
                .fetchOne();
    }
}
