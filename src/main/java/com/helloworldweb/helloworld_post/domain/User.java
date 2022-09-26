package com.helloworldweb.helloworld_post.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @NotNull
    private String email;
    private String profileUrl;
    private String nickName;

    @Builder
    public User(Long id, String email, String profileUrl, String nickName) {
        this.id = id;
        this.email = email;
        this.profileUrl = profileUrl;
        this.nickName = nickName;
    }
}
