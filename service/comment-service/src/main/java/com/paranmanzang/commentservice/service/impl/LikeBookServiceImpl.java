package com.paranmanzang.commentservice.service.impl;

import com.paranmanzang.commentservice.model.domain.BookResponseModel;
import com.paranmanzang.commentservice.model.domain.LikeBookModel;
import com.paranmanzang.commentservice.model.entity.LikeBooks;
import com.paranmanzang.commentservice.model.repository.BookRepository;
import com.paranmanzang.commentservice.model.repository.LikeBooksRepository;
import com.paranmanzang.commentservice.service.LikeBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeBookServiceImpl implements LikeBookService {
    private final LikeBooksRepository likeBooksRepository;
    private final BookRepository bookRepository;

    @Override
    public Object add(LikeBookModel likeBookModel) throws BindException {
        return bookRepository.findById(likeBookModel.getBookId())
                .map(book -> {
                    if (likeBooksRepository.existsByNicknameAndBook(likeBookModel.getNickname(), book)) {
                        return false;
                    }
                    LikeBookModel.fromEntity(likeBooksRepository.save(LikeBooks.builder()
                            .nickname(likeBookModel.getNickname())
                            .book(book)
                            .build()));
                    return BookResponseModel.fromEntity(bookRepository.findById(likeBookModel.getBookId()).get());
                })
                .orElseThrow(() -> {
                    var bindException = new BindException(likeBookModel, "likeBookModel");
                    bindException.addError(new FieldError("LikeBookModel", "id", "해당 책을 찾을 수 없습니다."));
                    return bindException;
                });
    }

    @Override
    public Boolean remove(LikeBookModel likeBookModel) throws BindException {
        return likeBooksRepository.findById(likeBookModel.getId())
                .map(likeBooks -> {
                    likeBooksRepository.deleteById(likeBookModel.getId());
                    return true;
                })
                .orElseThrow(() -> {
                    var bindException = new BindException(likeBookModel, "likeBookModel");
                    bindException.addError(new FieldError("LikeBookModel", "id", "해당 책을 찾을 수 없습니다."));
                    return bindException;
                });
    }

    @Override
    public List<BookResponseModel> findAllByNickname(String nickname) {
        return likeBooksRepository.findLikeBooksByNickname(nickname);
    }
}
