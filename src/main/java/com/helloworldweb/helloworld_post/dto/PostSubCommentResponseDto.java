package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.PostSubComment;
import lombok.Builder;
import lombok.Data;

@Data
public class PostSubCommentResponseDto {
    private Long id;
    private UserResponseDto userResponseDto;
    private String content;

    @Builder
    public PostSubCommentResponseDto(Long id, UserResponseDto userResponseDto, String content) {
        this.id = id;
        this.userResponseDto = userResponseDto;
        this.content = content;
    }

    public PostSubCommentResponseDto(PostSubComment postSubComment){
        this.id = postSubComment.getId();
        this.userResponseDto = new UserResponseDto(postSubComment.getWriter());
        this.content = postSubComment.getContent();
    }
}
