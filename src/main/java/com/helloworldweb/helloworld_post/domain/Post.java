package com.helloworldweb.helloworld_post.domain;

import com.helloworldweb.helloworld_post.dto.PostRequestDto;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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
        this.postComments = postComments==null? new ArrayList<>() : postComments;
        this.postImages = postImages==null? new ArrayList<>() : postImages;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.searchCounts = searchCounts;
        this.views = views;
        this.solved = solved;
    }

    // 어떤 유저가 작성했는지에 대한 연관관계 설정
    public Post changeUser(User user){
        this.user = user;
        user.addPost(this);
        return this;
    }

    public void changeTitle(String title){
        this.title = title;
    }
    public void changeContent(String content){
        this.title = content;
    }

    public void addPostComment(PostComment postComment){
        this.postComments.add(postComment);
        postComment.changePost(this);
    }

    public Post changeWithDto(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.tags = postRequestDto.getTags();
        return this;
    }

}
