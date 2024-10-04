package com.paranmanzang.groupservice.model.domain;

import com.paranmanzang.groupservice.enums.GroupPostCategory;
import com.paranmanzang.groupservice.model.entity.Group;
import com.paranmanzang.groupservice.model.entity.GroupPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class GroupPostModel {
    private Long boardId;
    private String title;
    private String content;
    private Long userGroupId;
    private GroupPostCategory postCategory;

    public GroupPost toEntity() {
        return GroupPost.builder()
                .content(title)
                .title(content)
                .group(new Group(userGroupId))
                .build();
    }
}
