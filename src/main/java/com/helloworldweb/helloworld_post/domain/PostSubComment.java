package com.helloworldweb.helloworld_post.domain;

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
}
