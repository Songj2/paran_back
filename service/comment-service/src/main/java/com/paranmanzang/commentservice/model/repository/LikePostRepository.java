package com.paranmanzang.commentservice.model.repository;

import com.paranmanzang.commentservice.model.entity.LikePosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<LikePosts, Long>, LikePostRepositoryCustom {

    LikePosts findByNicknameAndPostId(String nickname, Long postId);

    int deleteByNicknameAndPostId(String nickname, Long postId);

    Boolean existsByNicknameAndPostId(String nickname, Long postId);



}
