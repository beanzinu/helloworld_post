package com.helloworldweb.helloworld_post.service;

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
        System.out.println("KAFKA:user_register consumer received = " + msg);
        // consume msg 파싱
        Long userId = Long.parseLong(JsonParser.parseString(msg).getAsJsonObject().get("id").getAsString());
        String email = JsonParser.parseString(msg).getAsJsonObject().get("email").getAsString();
        String profileUrl = JsonParser.parseString(msg).getAsJsonObject().get("profileUrl").getAsString();
        // 파싱한 유저정보로 DB에 저장
        User newUser = User.builder().id(userId).email(email).profileUrl(profileUrl).build();
        userRepository.save(newUser);

        System.out.println("KAFKA:user_register consumer parsed userID = "+ userId);
    }
}
