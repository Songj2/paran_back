package com.paranmanzang.commentservice.model.repository;

import com.paranmanzang.commentservice.model.domain.BookResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<BookResponseModel> findAllBooks(Pageable pageable);
}
