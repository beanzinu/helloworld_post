package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.model.ApiResponse;
import com.helloworldweb.helloworld_post.model.HttpResponseMsg;
import com.helloworldweb.helloworld_post.model.HttpStatusCode;
import com.helloworldweb.helloworld_post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/post")
    public ResponseEntity<ApiResponse<?>> registerPost(PostRequestDto postRequestDto,
           @CookieValue(value = "email",required = true) String email)
    {
        PostResponseDto postResponseDto = postService.addPost(postRequestDto, email);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postResponseDto),HttpStatus.OK);
    }

    @GetMapping("/api/post/user")
    public ResponseEntity<ApiResponse<?>> getPostByUserId(@Param(value = "id")Long userId){
        List<PostResponseDto> postResponseDto = postService.getAllPostByUserId(userId);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postResponseDto),HttpStatus.OK);
    }

}
