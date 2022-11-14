package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDtoWithPageNum;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {


    /**
     * CREATE : 게시물 작성
     * @param postRequestDto : 게시물의 내용
     * @param userId : 유저의 PK
     * @return : PostResponseDto
     */
    PostResponseDto addPost(PostRequestDto postRequestDto,Long userId);

    /**
     * READ : 게시물 조회
     * @param postId : 게시물의 PK
     * @return : PostResponseDto
     */
    PostResponseDto getPost(Long postId);

    /**
     * UPDATE : 게시물 수정
     * @param postRequestDto : 게시물의 내용
     * @param userId : 유저의 PK
     * @return : PostResponseDto
     */
    PostResponseDto updatePost(PostRequestDto postRequestDto,Long userId);

    /**
     * DELETE : 게시물 삭제
     * @param postId : 게시물의 PK
     * @param userId : 유저의 PK
     * @return : Boolean
     */
    void deletePost(Long postId,Long userId);

    /**
     * READ : 해당 이메일의 유저가 작성한 모든 게시물 조회
     * @param userId : 유저의 PK
     * @return : List<PostResponseDto>
     */
    List<PostResponseDto> getAllPostByUserId(Long userId);

    /**
     * READ : 해당 페이지의 모든 게시물 조회
     * @param pageable : 페이지와 사이즈를 담고있는 Pageable 객체
     * @return : PostResponseDtoWithPageNum
     */
    PostResponseDtoWithPageNum getAllPostByPage(Pageable pageable);

    /**
     * READ : 상위 질문들 조회
     * @return : List<PostResponseDto>
     */
    List<PostResponseDto> getTopQuestions();

    /**
     * READ : 질문들 검색결과 조회
     * @param sentence : 검색 문장
     * @param pageable : 페이지 객체
     * @return : PostResponseDtoWithPageNum
     */
    PostResponseDtoWithPageNum findPostListWithPageAndSentence(String sentence,Pageable pageable);
}
