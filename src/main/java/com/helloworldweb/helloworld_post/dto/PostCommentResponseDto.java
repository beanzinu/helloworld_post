package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.PostComment;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostCommentResponseDto {
    private Long id;
    private Long post_id;
    private List<PostSubCommentResponseDto> postSubCommentResponseDtoList = new ArrayList<>();


    @Builder
    public PostCommentResponseDto(Long id, Long post_id, List<PostSubCommentResponseDto> postSubCommentResponseDtoList) {
        this.id = id;
        this.post_id = post_id;
        this.postSubCommentResponseDtoList = postSubCommentResponseDtoList;
    }


    public PostCommentResponseDto(PostComment postComment){
        this.id = postComment.getId();
        this.post_id = postComment.getPost().getId();
        this.postSubCommentResponseDtoList = postComment.getPostSubComments().stream()
                                .map(PostSubCommentResponseDto::new)
                                .collect(Collectors.toList());
    }
}
