package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;

import java.util.List;

public interface PostService {


    /**
     * CREATE : 게시물 작성
     * @param postRequestDto : 게시물의 내용
     * @param email : 유저의 이메일
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
     * @param email : 유저의 이메일
     * @return : PostResponseDto
     */
    PostResponseDto updatePost(PostRequestDto postRequestDto,String email);

    /**
     * DELETE : 게시물 삭제
     * @param postId : 게시물의 PK
     * @param email : 유저의 이메일
     * @return : Boolean
     */
    void deletePost(Long postId,String email);

    /**
     * READ : 해당 이메일의 유저가 작성한 모든 게시물 조회
     * @param userId : 유저의 이메일
     * @return : List<PostResponseDto>
     */
    List<PostResponseDto> getAllPostByUserId(Long userId);
}
