package com.helloworldweb.helloworld_post.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class PostComment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL)
    private List<PostSubComment> postSubComments = new ArrayList<>();

    @ManyToMany
    private List<User> engagingUserList = new ArrayList<>();

    // 채택여부
    private boolean selected;

    @Builder
    public PostComment(Long id, Post post, List<PostSubComment> postSubComments, List<User> engagingUserList, boolean selected) {
        this.id = id;
        this.post = post;
        this.postSubComments = postSubComments;
        this.engagingUserList = engagingUserList;
        this.selected = selected;
    }

}
