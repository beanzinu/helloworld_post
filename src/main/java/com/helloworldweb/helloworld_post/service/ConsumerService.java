package com.helloworldweb.helloworld_post.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final UserRepository userRepository;

    @KafkaListener(topics= "user_server")
    public void userRegisterListener(String msg, Acknowledgment ack){
        // consume msg 파싱
        String op = getOpertaion(msg);
        Map<String,String> map = messageToMap(msg);
        switch (op){
            case "register":
                User user = mapToUser(map);
                userRepository.save(user);
                break;
            case "update":
                User findUser = userRepository.findById(Long.valueOf(map.get("id"))).orElseThrow(()-> new IllegalArgumentException());
                findUser.updateUser(map);
                userRepository.save(findUser);
                break;
            case "delete":
                User deleteUser = userRepository.findById(Long.valueOf(map.get("id"))).orElseThrow(()-> new IllegalArgumentException());
                userRepository.delete(deleteUser);
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 작업입니다.");
        }

        ack.acknowledge();

    }

    private String getOpertaion(String jsonString)
    {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject object = element.getAsJsonObject();

        return object.get("operation").getAsString();

    }

    private Map<String,String> messageToMap(String jsonString ){
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject object = element.getAsJsonObject();

        Map<String,String> map = new HashMap<>();

        Long id = object.get("id").getAsLong();
        String email = object.has("email") ? object.get("email").getAsString(): null;
        String socialAccountId = object.has("socialAccountId")? object.get("socialAccountId").getAsString():null;
        String nickName = object.has("nickName")? object.get("nickName").getAsString():null;
        String profileUrl = object.has("profileUrl")?object.get("profileUrl").getAsString():null;
        String repoUrl = object.has("repoUrl") ? object.get("repoUrl").getAsString() : null;
        String profileMusicName = object.has("profileMusicName")?object.get("profileMusicName").getAsString():null;
        String profileMusicUrl = object.has("profileMusicUrl")? object.get("profileMusicUrl").getAsString():null;
        String fcm = object.has("fcm")?object.get("fcm").getAsString():null;

        map.put("id",String.valueOf(id));
        map.put("email",email);
        map.put("socialAccountId",socialAccountId);
        map.put("nickName",nickName);
        map.put("profileUrl",profileUrl);
        map.put("repoUrl",repoUrl);
        map.put("profileMusicName",profileMusicName);
        map.put("profileMusicUrl",profileMusicUrl);
        map.put("fcm",fcm);

        return map;
    }

    private User mapToUser(Map<String,String> map)
    {
        return User.builder()
                .id(Long.valueOf(map.get("id")))
                .email(map.get("email"))
                .profileUrl(map.get("profileUrl"))
                .nickName(map.get("nickName"))
                .build();
    }
}
