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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class PostSubCommentController {
    private final PostSubCommentService postSubCommentService;

    @GetMapping(value = "/api/post/question/comment/user")
    public ResponseEntity<ApiResponse> getPostSubCommentListByUserId(
            @RequestParam(value = "userId") Long userId
    ){
        List<PostSubCommentResponseDto> postSubCommentListByUserId = postSubCommentService.getPostSubCommentListByUserId(userId);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,postSubCommentListByUserId), HttpStatus.OK);
    }

    /**
     * POST : 댓글/대댓글 작성
     * @param postId : 게시물 PK
     * @param postCommentId : 댓글 PK
     * @param postSubCommentRequestDto : 댓글 내용 DTO
     * @return
     */
    @PostMapping(value = "/api/post/question/comment")
    public ResponseEntity<ApiResponse> registerPostSubComment(
            @RequestParam(value = "postId",required = false) Long postId,
            @RequestParam(value = "postCommentId",required = false) Long postCommentId,
            @RequestBody PostSubCommentRequestDto postSubCommentRequestDto
    ){
        PostSubCommentResponseDto postSubCommentResponseDto = postSubCommentService.addPostSubComment(postId, postCommentId, postSubCommentRequestDto);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postSubCommentResponseDto), HttpStatus.OK);
    }

    /**
     * PUT : 댓글 수정
     * @param postSubCommentRequestDto : 수정 내용 DTO
     * @return
     */
    @PutMapping(value = "/api/post/question/comment")
    public ResponseEntity<ApiResponse> updatePostSubComment(
            @RequestBody PostSubCommentRequestDto postSubCommentRequestDto
    ){
        PostSubCommentResponseDto postSubCommentResponseDto = postSubCommentService.updatePostSubComment(postSubCommentRequestDto);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.PUT_SUCCESS,
                HttpResponseMsg.PUT_SUCCESS,postSubCommentResponseDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/post/question/comment")
    public ResponseEntity<ApiResponse> deletePostSubComment(
            @RequestParam(value = "postSubCommentId") Long postSubCommentId
    ){
        postSubCommentService.deletePostSubComment(postSubCommentId);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.DELETE_SUCCESS,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }

}
