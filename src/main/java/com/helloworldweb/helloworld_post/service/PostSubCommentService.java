package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.dto.PostCommentResponseDto;
import com.helloworldweb.helloworld_post.dto.PostSubCommentRequestDto;
import com.helloworldweb.helloworld_post.dto.PostSubCommentResponseDto;

import java.util.List;

public interface PostSubCommentService {

    /**
     * CREATE : 댓글 등록 : 첫 댓글 / 두번째~N번째 댓글 로직 분리
     * @param postId : 게시물 PK
     * @param  postCommentId : 게시물 특정댓글 PK
     * @param postSubCommentRequestDto : 댓글 DTO
     * @return : PostSubCommentResponseDto
     */
    PostSubCommentResponseDto addPostSubComment(Long postId, Long postCommentId, PostSubCommentRequestDto postSubCommentRequestDto);

    /**
     * READ : 해당 게시물의 댓글들 조회
     * @param postId : 게시물 PK
     * @return : List<PostCommentResponseDto>
     */
    List<PostCommentResponseDto> getPostCommentListByPostId(Long postId);


    /**
     * READ : 특정 유저가 작성한 댓글 조회
     * @param userId : 유저 PK
     * @return : List<PostSubCommentResponseDto>
     */
    List<PostSubCommentResponseDto> getPostSubCommentListByUserId(Long userId);

    /**
     * UPDATE : 해당 댓글의 내용 수정
     * @param postSubCommentRequestDto : 댓글 수정내용 DTO
     * @return : PostSubCommentResponseDto
     */
    PostSubCommentResponseDto updatePostSubComment(PostSubCommentRequestDto postSubCommentRequestDto);

    /**
     * DELETE : 해당 댓글 삭제
     * @param postSubCommentId : 댓글 PK
     */
    void deletePostSubComment(Long postSubCommentId);

}
