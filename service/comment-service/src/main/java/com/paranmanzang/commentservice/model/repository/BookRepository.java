package com.paranmanzang.commentservice.model.repository;

import com.paranmanzang.commentservice.model.entity.Book;
import com.paranmanzang.commentservice.model.repository.BookRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> , BookRepositoryCustom {
}
