package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.dto.UserRequestDto;
import com.helloworldweb.helloworld_post.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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


}
