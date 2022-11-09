package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.PostSubComment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PostSubCommentResponseDto {
    private Long id;
    private PostResponseDto postResponseDto;
    private UserResponseDto userResponseDto;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    @Builder
    public PostSubCommentResponseDto(Long id, PostResponseDto postResponseDto, UserResponseDto userResponseDto, String content, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.postResponseDto = postResponseDto;
        this.userResponseDto = userResponseDto;
        this.content = content;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }


    public PostSubCommentResponseDto(PostSubComment postSubComment){
        this.id = postSubComment.getId();
        this.userResponseDto = postSubComment.getWriter() == null ? null :
                new UserResponseDto(postSubComment.getWriter());
        this.content = postSubComment.getContent();
        this.createdTime = postSubComment.getCreatedTime();
        this.modifiedTime = postSubComment.getModifiedTime();
    }

    // 유저가 작성한 댓글 조회용 DTO
    public static PostSubCommentResponseDto getPostSubCommentResponseDtoWithPostResponseDto(PostSubComment postSubComment){
        return PostSubCommentResponseDto.builder()
                .postResponseDto(new PostResponseDto(postSubComment.getPostComment().getPost()))
                .id(postSubComment.getId())
                .content(postSubComment.getContent())
                .build();
    }
}
