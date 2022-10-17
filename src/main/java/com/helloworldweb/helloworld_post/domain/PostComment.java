package com.helloworldweb.helloworld_post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class PostComment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL)
    private List<PostSubComment> postSubComments = new ArrayList<>();

    // 채택여부
    private boolean selected;

    @Builder
    public PostComment(Long id, Post post, List<PostSubComment> postSubComments, boolean selected) {
        this.id = id;
        this.post = post;
        this.postSubComments = postSubComments==null ? new ArrayList<>() : postSubComments;
        this.selected = selected;
    }

    public void addPostSubComment(PostSubComment postSubComment){
        this.postSubComments.add(postSubComment);
        postSubComment.changePostComment(this);
    }

    public void changePost(Post post){
        this.post = post;
    }
}
