package com.helloworldweb.helloworld_post.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final UserRepository userRepository;

    @KafkaListener(topics= "user_register")
    public void userRegisterListener(String msg, Acknowledgment ack){
        // consume msg 파싱
        JsonElement jsonElement = JsonParser.parseString(msg);
        Long userId = null;
        String email = null;
        String profileUrl = null;
        String nickName = null;
        if(jsonElement.getAsJsonObject().has("id")){
            userId = Long.parseLong(jsonElement.getAsJsonObject().get("id").getAsString());
        }
        if(jsonElement.getAsJsonObject().has("email")){
            email = jsonElement.getAsJsonObject().get("email").getAsString();
        }
        if(jsonElement.getAsJsonObject().has("profileUrl")){
            profileUrl = jsonElement.getAsJsonObject().get("profileUrl").getAsString();
        }
        if(jsonElement.getAsJsonObject().has("nickName")){
            nickName= jsonElement.getAsJsonObject().get("nickName").getAsString();
        }

        // 파싱한 유저정보로 DB에 저장
        if ( userId != null && email != null && profileUrl != null ) {
            User newUser = User.builder().id(userId).email(email).profileUrl(profileUrl).nickName(nickName).build();
            userRepository.save(newUser);
        }
    }
}
