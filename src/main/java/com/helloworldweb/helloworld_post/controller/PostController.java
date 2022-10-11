package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.model.ApiResponse;
import com.helloworldweb.helloworld_post.model.HttpResponseMsg;
import com.helloworldweb.helloworld_post.model.HttpStatusCode;
import com.helloworldweb.helloworld_post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    public ResponseEntity<ApiResponse<?>> registerPost(PostRequestDto postRequestDto,
           @CookieValue(value = "email",required = true) String email)
    {
        try {
            PostResponseDto postResponseDto = postService.addPost(postRequestDto, email);
            return new ResponseEntity(ApiResponse.response(
                    HttpStatusCode.POST_SUCCESS,
                    HttpResponseMsg.POST_SUCCESS,postResponseDto),HttpStatus.OK);
        } catch ( RuntimeException e ){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.INTERNAL_SERVER_ERROR,
                    HttpResponseMsg.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
