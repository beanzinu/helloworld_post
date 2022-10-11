package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequestDto {
    private Long id;
    private String email;
    private String profileUrl;
    private String nickName;

    @Builder
    public UserRequestDto(Long id, String email, String profileUrl, String nickName) {
        this.id = id;
        this.email = email;
        this.profileUrl = profileUrl;
        this.nickName = nickName;
    }
    public User toEntity(){
        return User.builder()
                .id(this.id)
                .email(this.email)
                .profileUrl(this.profileUrl)
                .nickName(this.nickName)
                .build();
    }
}

