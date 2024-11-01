package com.paranmanzang.commentservice.model.repository.impl;

import com.paranmanzang.commentservice.model.domain.GroupPostResponseModel;
import com.paranmanzang.commentservice.model.repository.LikePostRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.paranmanzang.commentservice.model.entity.QBook.book;
import static com.paranmanzang.commentservice.model.entity.QGroup.group;
import static com.paranmanzang.commentservice.model.entity.QGroupPost.groupPost;
import static com.paranmanzang.commentservice.model.entity.QLikePosts.likePosts;

@RequiredArgsConstructor
public class LikePostRepositoryImpl implements LikePostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupPostResponseModel> findLikePostByNickname(String nickname) {
        var ids = queryFactory
                .select(likePosts.postId)
                .from(likePosts)
                .where(
                        likePosts.nickname.eq(nickname)
                )
                .orderBy(likePosts.id.desc())
                .fetch();

        // Step 2: 필요한 필드 조회 및 GroupPostResponseModel 변환
        return ids.isEmpty() ? List.of() :
                queryFactory
                        .selectDistinct(Projections.constructor(
                                GroupPostResponseModel.class,
                                groupPost.id,
                                groupPost.title,
                                groupPost.content,
                                groupPost.createAt,
                                groupPost.modifyAt,
                                groupPost.postCategory,
                                groupPost.viewCount,
                                groupPost.nickname,
                                groupPost.group.id.as("groupId"),
                                groupPost.group.name.as("groupName"),
                                groupPost.book.id.as("bookId"),
                                groupPost.book.title.as("bookTitle")
                        ))
                        .from(groupPost)
                        .leftJoin(groupPost.group, group)
                        .leftJoin(groupPost.book, book)
                        .where(groupPost.id.in(ids))
                        .fetch();
    }
}
