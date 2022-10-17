package com.helloworldweb.helloworld_post.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
//@Table(indexes = {@Index(name = "email_index",columnList = "email")})
public class User {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<PostSubComment> subComments = new ArrayList<>();

    @NotNull
    private String email;
    private String profileUrl;
    private String nickName;

    @Builder
    public User(Long id, String email, String profileUrl, String nickName,List<PostSubComment> postSubComments) {
        this.id = id;
        this.email = email;
        this.posts = new ArrayList<>();
        this.profileUrl = profileUrl;
        this.nickName = nickName;
        this.subComments = postSubComments==null ? new ArrayList<>() : postSubComments;
    }

    public void addPost(Post post){
        this.posts.add(post);
    }

}
