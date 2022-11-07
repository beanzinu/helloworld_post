package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.dto.PostSubCommentRequestDto;
import com.helloworldweb.helloworld_post.dto.PostSubCommentResponseDto;

public interface PostSubCommentService {

    /**
     * 댓글 등록 : 첫 댓글 / 두번째~N번째 댓글 로직 분리
     * @param postId : 게시물 PK
     * @param  postCommentId : 게시물 특정댓글 PK
     * @param postSubCommentRequestDto : 댓글 DTO
     * @return : PostSubCommentResponseDto
     */
    PostSubCommentResponseDto addPostSubComment(Long postId, Long postCommentId, PostSubCommentRequestDto postSubCommentRequestDto);
}
