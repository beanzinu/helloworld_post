package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.model.ApiResponse;
import com.helloworldweb.helloworld_post.model.HttpResponseMsg;
import com.helloworldweb.helloworld_post.model.HttpStatusCode;
import com.helloworldweb.helloworld_post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * POST : 질문 등록
     * @param postRequestDto : 질문내용 (HTTP BODY)
     * @return : postResponseDto
     */
    @PostMapping("/api/post")
    public ResponseEntity<ApiResponse<?>> registerPost(PostRequestDto postRequestDto)
    {
        User caller = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostResponseDto postResponseDto = postService.addPost(postRequestDto,caller.getId());
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postResponseDto),HttpStatus.OK);
    }

    /**
     * GET : 특정 유저의 질문 검색
     * @param userId : 유저의 PK
     * @return : List<PostResponseDto>
     */
    @GetMapping("/api/post/user")
    public ResponseEntity<ApiResponse<?>> getPostByUserId(@Param(value = "id")Long userId){
        List<PostResponseDto> postResponseDto = postService.getAllPostByUserId(userId);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postResponseDto),HttpStatus.OK);
    }

    /**
     * GET : 해당 페이지의 질문들 조회
     * @param pageable : 페이지 정보
     * @return : List<PostResponseDto>
     */
    @GetMapping("/api/post/qnasPage")
    public ResponseEntity<ApiResponse<?>> getPostByPage(
            @PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
        List<PostResponseDto> postResponseDto = postService.getAllPostByPage(pageable);
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postResponseDto),HttpStatus.OK);
    }

    /**
     * GET : 상위 X개 질문들 조회
     * @return : List<PostResponseDto>
     */
    @GetMapping("/api/post/top-questions")
    public ResponseEntity<ApiResponse<?>> getTopQuestions(){
        List<PostResponseDto> postResponseDto = postService.getTopQuestions();
        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,postResponseDto),HttpStatus.OK);
    }

}
