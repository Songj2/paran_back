package com.paranmanzang.commentservice.controller;

import com.paranmanzang.commentservice.model.domain.GroupPostModel;
import com.paranmanzang.commentservice.service.impl.GroupPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/posts")
@RequiredArgsConstructor
public class GroupPostController {
    private final GroupPostServiceImpl groupPostService;

    //#67 게시글 등록
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody GroupPostModel groupPostModel) {
        return ResponseEntity.ok(groupPostService.savePost(groupPostModel));
    }

    //#68. 게시글 수정
    @PutMapping
    public ResponseEntity<?> update(@RequestBody GroupPostModel groupPostModel) {
        return ResponseEntity.ok(groupPostService.updatePost(groupPostModel));
    }

    //#69.게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(groupPostService.deletePost(postId));
    }

    //#70 - 내가 속한 group의 게시물 목록
    @GetMapping("/{groupId}")
    public ResponseEntity<?> findByGroupId(@PathVariable("groupId") Long groupId, @RequestParam int page, @RequestParam int size, @RequestParam String postCategory) {
        return ResponseEntity.ok(groupPostService.findByGroupId(groupId, PageRequest.of(page, size), postCategory));
    }

    // view 카운트 업데이트
    @PutMapping("/{postId}")
    public ResponseEntity<?> updateViewCount(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(groupPostService.updateViewCount(postId));
    }
}