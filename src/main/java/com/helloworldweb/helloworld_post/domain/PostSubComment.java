package com.helloworldweb.helloworld_post.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class PostSubComment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String content;

    @Builder
    public PostSubComment(Long id, PostComment postComment, User user, String content)
    {
        this.id = id;
        this.postComment = postComment;
        this.user = user;
        this.content = content;
    }
}
