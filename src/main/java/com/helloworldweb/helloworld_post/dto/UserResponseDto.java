package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String profileUrl;
    private String nickName;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
        this.nickName = user.getNickName();
    }

    @Builder
    public UserResponseDto(Long id, String email, String profileUrl, String nickName) {
        this.id = id;
        this.email = email;
        this.profileUrl = profileUrl;
        this.nickName = nickName;
    }
}
