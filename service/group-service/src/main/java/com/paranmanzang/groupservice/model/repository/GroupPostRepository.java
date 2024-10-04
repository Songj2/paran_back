package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.enums.GroupPostCategory;
import com.paranmanzang.groupservice.model.entity.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long>, GroupPostRepositoryCustom {
    GroupPost getGroupPostById(Long boardId); //한 게시글

    List<?> findAllByPostCategory(GroupPostCategory postCategory); //카테고리 별 게시물

}
