package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponseDto {
    private Long post_id;
    private Long user_id;
    private String content;
    private String title;
    private String tags;
    private Long views;
    private List<PostCommentResponseDto> postCommentResponseDtoList;
    private UserResponseDto userResponseDto;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public PostResponseDto(Post post){
        this.post_id = post.getId();
        this.user_id = post.getUser().getId();
        this.content = post.getContent();
        this.title = post.getTitle();
        this.tags = post.getTags();
        this.views = post.getViews();
        this.createdTime = post.getCreatedTime();
        this.modifiedTime = post.getModifiedTime();
    }

    @Builder
    public PostResponseDto(Long post_id, Long user_id, String content, String title, String tags,UserResponseDto userResponseDto,Long views,LocalDateTime createdTime,LocalDateTime modifiedTime) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.title = title;
        this.tags = tags;
        this.userResponseDto = userResponseDto;
        this.views = views;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    // PostResponseDto + UserResponseDto
    public static PostResponseDto getDtoWithUser(Post post){
        return PostResponseDto.builder()
                .content(post.getContent())
                .title(post.getTitle())
                .post_id(post.getId())
                .tags(post.getTags())
                .views(post.getViews())
                .userResponseDto(new UserResponseDto(post.getUser()))
                .createdTime(post.getCreatedTime())
                .modifiedTime(post.getModifiedTime())
                .build();
    }
}
