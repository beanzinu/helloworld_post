package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;

public interface PostService {


    /**
     * CREATE : 게시물 작성
     * @param postRequestDto : 게시물의 내용
     * @param email : 쿠키 안의 이메일
     * @return : PostResponseDto
     */
    PostResponseDto addPost(PostRequestDto postRequestDto,String email);

    /**
     * READ : 게시물 조회
     * @param postId : 게시물의 PK
     * @return : PostResponseDto
     */
    PostResponseDto getPost(Long postId);

    /**
     * UPDATE : 게시물 수정
     * @param postRequestDto : 게시물의 내용
     * @param email : 쿠키 안의 이메일
     * @return : PostResponseDto
     */
    PostResponseDto updatePost(PostRequestDto postRequestDto,String email);

    /**
     * DELETE : 게시물 삭제
     * @param postId : 게시물의 PK
     * @param email : 쿠키 안의 이메일
     * @return : Boolean
     */
    void deletePost(Long postId,String email);
}
