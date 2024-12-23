package com.paranmanzang.commentservice.model.repository.impl;

import com.paranmanzang.commentservice.model.domain.BookResponseModel;
import com.paranmanzang.commentservice.model.repository.BookRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.paranmanzang.commentservice.model.entity.QBook.book;
import static com.paranmanzang.commentservice.model.entity.QLikeBooks.likeBooks;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookResponseModel> findAllBooks(Pageable pageable) {
        // Step 1: ID만 조회
        var ids = queryFactory
                .select(book.id)
                .from(book)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Step 2: 필요한 필드 조회 및 BookResponseModel 변환
        List<BookResponseModel> books = ids.isEmpty() ? List.of() :
                queryFactory
                        .selectDistinct(Projections.constructor(
                                BookResponseModel.class,
                                book.id.as("id"),
                                book.title.as("title"),
                                book.author.as("author"),
                                book.categoryName.as("categoryName"),
                                book.like_books.size().as("likeBookCount")
                        ))
                        .from(book)
                        .leftJoin(book.like_books, likeBooks)
                        .where(book.id.in(ids))
                        .fetch();

        long totalCount = Optional.ofNullable(queryFactory
                .select(book.id.count())
                .from(book)
                .fetchOne()).orElse(0L);
        return new PageImpl<>(books, pageable, totalCount);
    }

}