package com.helloworldweb.helloworld_post.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    // 제목
    private String title;
    // 내용
    @Lob
    private String content;
    // 태그
    private String tags;
    // 검색횟수
    private Long searchCounts = 0L;
    // 조회수
    private Long views = 0L;
    // 해결여부
    private boolean solved;

    @Builder
    public Post(Long id, User user, List<PostComment> postComments, List<PostImage> postImages, String title, String content, String tags, Long searchCounts, Long views, boolean solved) {
        this.id = id;
        this.user = user;
        this.postComments = postComments;
        this.postImages = postImages;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.searchCounts = searchCounts;
        this.views = views;
        this.solved = solved;
    }

}
