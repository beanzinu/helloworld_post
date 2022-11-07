package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.NoResultException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.MissingFormatArgumentException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    /**
     * READ : 이메일을 통한 유저조회
     * @param email : 이메일
     * @return : User 객체
     */
    @Override
    public User getUserByEmail(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow(()->{
            throw new NoResultException(email);
        });
        return findUser;
    }

    /**
     * READ : Id를 통한 유저조회
     * @param id : 유저 PK
     * @return : User
     */
    @Override
    public User getUserById(Long id) {
        User findUser = userRepository.findById(id).orElseGet(() -> getUserFromUserServer(id));

        return findUser;
    }

    /**
     * 유저 서버로부터 유저정보 받아오기
     * @param userId : 유저서버의 유저 PK
     * @return
     */
    private User getUserFromUserServer(Long userId){
        try {
            // RestTemplate 통해서 유저정보 받아오기
            RestTemplate restTemplate = new RestTemplate();
            URI uri = UriComponentsBuilder
                    .fromUriString("http://localhost:8080")
                    .path("/api/user")
                    .queryParam("id",userId)
                    .encode()
                    .build()
                    .toUri();
            ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(uri,JSONObject.class);
            // Http Body 파싱
            JSONObject data = responseEntity.getBody();
            JSONObject body = new JSONObject((LinkedHashMap) data.get("data"));
            Long id =   Integer.toUnsignedLong((Integer) body.get("id"));
            String email = (String) body.get("email");
            String profileUrl = (String) body.get("profileUrl");
            String nickName = (String) body.get("nickName");

            if ( id != null && email != null && profileUrl != null ){
                User newUser = User.builder()
                        .id(id)
                        .email(email)
                        .nickName(nickName)
                        .profileUrl(profileUrl)
                        .build();
                User savedUser = userRepository.save(newUser);
                log.info("getUserFromUserServer : save 유저 id: " + savedUser.getId());
                return savedUser;
            } else {
                log.warn("getUserFromUserServer : 유저정보 부족");
                throw new MissingFormatArgumentException("유저정보 부족");
            }


        } catch( RuntimeException e ) {
            log.warn("getUserFromUserServer : 유저정보 받아오기 실패 ");
            e.printStackTrace();
            throw new NoResultException();
        }
    }

    /**
     * JWT 토큰 내의 유저정보로 유저조회
     * @return : User
     */
    @Override
    public User getCaller() {
        Long id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return userRepository.findById(id)
                .orElseThrow(()->new NoResultException("UserServiceImpl - getCaller : 유저를 찾을 수 없음."));
    }
}
