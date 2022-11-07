package com.helloworldweb.helloworld_post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSubCommentRequestDto {
    private String content;

    @Builder
    public PostSubCommentRequestDto(String content) {
        this.content = content;
    }
}
