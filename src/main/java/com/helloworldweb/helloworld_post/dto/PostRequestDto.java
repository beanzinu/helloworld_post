package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.Post;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class PostRequestDto { // post 요청과 함께 받을 데이터
    private Long post_id;
    private Long user_id;
    private String content;
    private String title;
    private String tags;

    // Dto -> Entity
    public Post toEntity() {
        return Post.builder()
                .id(post_id)
                .title(title)
                .content(content)
                .tags(tags)
                .build();
    }

    @Builder
    public PostRequestDto(Long post_id, Long user_id, String content, String title, String tags) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.title = title;
        this.tags = tags;
    }
}
