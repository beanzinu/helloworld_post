package com.helloworldweb.helloworld_post.domain;

import com.helloworldweb.helloworld_post.dto.PostSubCommentRequestDto;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PostSubComment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @NotNull
    private String content;

    @Builder
    public PostSubComment(Long id, PostComment postComment, User writer, String content)
    {
        this.id = id;
        this.postComment = postComment;
        this.writer = writer;
        this.content = content;
    }

    public void changePostComment(PostComment postComment){
        this.postComment = postComment;
    }

    // DTO 로 변경
    public void changeWithDto(PostSubCommentRequestDto postSubCommentRequestDto){
        this.content = postSubCommentRequestDto.getContent();
    }

    public void delete(){
        this.writer = null;
        this.content = "삭제된 댓글입니다.";
    }
}
