package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.dto.PostSubCommentRequestDto;
import com.helloworldweb.helloworld_post.dto.PostSubCommentResponseDto;
import com.helloworldweb.helloworld_post.model.ApiResponse;
import com.helloworldweb.helloworld_post.model.HttpResponseMsg;
import com.helloworldweb.helloworld_post.model.HttpStatusCode;
import com.helloworldweb.helloworld_post.service.PostSubCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostSubCommentController {
    private final PostSubCommentService postSubCommentService;

    @PostMapping(value = "/api/post/question/comment")
    public ResponseEntity<ApiResponse> registerPostSubComment(
            @RequestParam(value = "postId",required = false) Long postId,
            @RequestParam(value = "postCommentId",required = false) Long postCommentId,
            @RequestBody PostSubCommentRequestDto postSubCommentRequestDto){
        PostSubCommentResponseDto postSubCommentResponseDto = postSubCommentService.addPostSubComment(postId, postCommentId, postSubCommentRequestDto);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postSubCommentResponseDto), HttpStatus.OK);
    }

}
