package com.helloworldweb.helloworld_post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSubCommentRequestDto {
    private Long id;
    private String content;

    @Builder
    public PostSubCommentRequestDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
