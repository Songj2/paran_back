package com.paranmanzang.commentservice.model.domain;

import com.paranmanzang.commentservice.model.entity.LikePosts;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LikePostModel {
    private Long id;
    private Long postId;
    private String nickname;


    public static LikePostModel fromEntity(LikePosts likeposts) {
        return LikePostModel.builder()
                .id(likeposts.getId())
                .postId(likeposts.getPostId())
                .nickname(likeposts.getNickname())
                .build();
    }
}
