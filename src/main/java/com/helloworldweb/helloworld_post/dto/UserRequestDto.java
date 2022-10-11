package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.json.simple.JSONObject;

@Data
@Getter
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

    // JSON -> UserRequestDto
    public static UserRequestDto jsonObjectToUserRequestDto(JSONObject jsonObject){
        return UserRequestDto.builder()
                .email((String)jsonObject.get("email"))
                .nickName((String)jsonObject.get("userName"))
                .profileUrl((String)jsonObject.get("profileUrl"))
                .build();
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

