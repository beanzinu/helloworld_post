package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.UserRequestDto;
import com.helloworldweb.helloworld_post.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.LinkedHashMap;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/user")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "*");
        userService.addUser(userRequestDto);
        return ResponseEntity.ok("user");
    }

    @GetMapping("/api/user")
    public ResponseEntity<String> getUser(@Param(value = "email")String email){
        User findUser = userService.getUserByEmail(email);
        return ResponseEntity.ok(findUser.getId().toString());
    }


    /**
     * 유저정보 조회에 실패했을 경우
     */
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<String> getUserAdvice(Exception e) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path("/msa/user")
                .queryParam("email",e.getMessage())
                .encode()
                .build()
                .toUri();

        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(uri,JSONObject.class);
        if (responseEntity.getStatusCode().isError()){
            return ResponseEntity.ok("인증서버에도 없는 유저");
        }
        JSONObject data = responseEntity.getBody();
        JSONObject body = new JSONObject((LinkedHashMap) data.get("data"));


        userService.addUser( UserRequestDto.jsonObjectToUserRequestDto(body) );
        // 임시
        return ResponseEntity.ok("인증서버로부터 저장성공");
    }

}
