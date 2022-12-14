package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.PostComment;
import com.helloworldweb.helloworld_post.domain.PostSubComment;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.PostCommentResponseDto;
import com.helloworldweb.helloworld_post.dto.PostSubCommentRequestDto;
import com.helloworldweb.helloworld_post.dto.PostSubCommentResponseDto;
import com.helloworldweb.helloworld_post.repository.PostCommentRepository;
import com.helloworldweb.helloworld_post.repository.PostRepository;
import com.helloworldweb.helloworld_post.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostSubCommentServiceImpl implements PostSubCommentService{


    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostSubCommentRepository postSubCommentRepository;
    private final UserService userService;

    /**
     * 댓글 등록 : 첫 댓글 / 두번째~N번째 댓글 로직 분리
     * @param postId        : 게시물 PK
     * @param postCommentId : 게시물 특정댓글 PK
     * @param postSubCommentRequestDto : 댓글 DTO
     * @return : PostSubCommentResponseDto
     */
    @Override
    public PostSubCommentResponseDto addPostSubComment(Long postId, Long postCommentId, PostSubCommentRequestDto postSubCommentRequestDto) {
        // 유저 식별
        User caller = userService.getCaller();

        // 첫 댓글 : 댓글
        if( postCommentId == null ){
            Post findPost = postRepository.findById(postId)
                    .orElseThrow(() -> new NoResultException("PostSubCommentServiceImpl - addPostSubComment : 게시물 없음"));

            PostComment newPostComment = PostComment.builder().build();
            PostSubComment newPostSubComment = PostSubComment.builder()
                    .writer(caller)
                    .content(postSubCommentRequestDto.getContent())
                    .build();

            findPost.addPostComment(newPostComment);
            newPostComment.addPostSubComment(newPostSubComment);
            postRepository.save(findPost);

            return new PostSubCommentResponseDto(newPostSubComment);
        }
        // 두번째 ~  : 대댓글
        else {
            PostComment findPostComment = postCommentRepository.findById(postCommentId)
                    .orElseThrow(() -> new NoResultException("PostSubCommentServiceImpl - addPostSubComment : 댓글 없음"));

            PostSubComment newPostSubComment = PostSubComment.builder()
                    .writer(caller)
                    .postComment(findPostComment)
                    .content(postSubCommentRequestDto.getContent())
                    .build();
            postSubCommentRepository.save(newPostSubComment);

            return new PostSubCommentResponseDto(newPostSubComment);
        }

    }

    /**
     * READ : 해당 게시물의 댓글들 조회
     * @param postId : 게시물 PK
     * @return : List<PostCommentResponseDto>
     */
    @Override
    public List<PostCommentResponseDto> getPostCommentListByPostId(Long postId) {
        return postCommentRepository.findAllPostCommentByPostId(postId)
                .stream().map(PostCommentResponseDto::new).collect(Collectors.toList());
    }

    /**
     * READ : 특정 유저가 작성한 댓글 조회
     * @param userId : 유저 PK
     * @return : List<PostSubCommentResponseDto>
     */
    @Override
    public List<PostSubCommentResponseDto> getPostSubCommentListByUserId(Long userId) {
        List<PostSubCommentResponseDto> findPostSubCommentDtoList = postSubCommentRepository.findAllByWriterId(userId)
                .stream().map(PostSubCommentResponseDto::getPostSubCommentResponseDtoWithPostResponseDto)
                .collect(Collectors.toList());
        return findPostSubCommentDtoList;
    }

    /**
     * UPDATE : 해당 댓글의 내용 수정
     * @param postSubCommentRequestDto : 댓글 수정내용 DTO
     * @return : PostSubCommentResponseDto
     */
    @Override
    @Transactional
    public PostSubCommentResponseDto updatePostSubComment(PostSubCommentRequestDto postSubCommentRequestDto) {
        User caller = userService.getCaller();
        PostSubComment findPostSubComment = postSubCommentRepository.findById(postSubCommentRequestDto.getId())
                .orElseThrow(() -> new NoResultException("PostSubCommentServiceImpl - updatePostSubComment : 댓글 없음"));
        // caller 와 해당 게시물의 user_id가 같을 때만 저장
        if ( caller.getId() == findPostSubComment.getWriter().getId() ){
            findPostSubComment.changeWithDto(postSubCommentRequestDto);
            return new PostSubCommentResponseDto(findPostSubComment);
        }
        else {
            throw new IllegalCallerException("PostSubCommentServiceImpl - updatePostSubComment : 권한 없음");
        }
    }

    /**
     * DELETE : 해당 댓글 삭제
     * @param postSubCommentId : 댓글 PK
     */
    @Override
    @Transactional
    public void deletePostSubComment(Long postSubCommentId) {
        User caller = userService.getCaller();
        PostSubComment findPostSubComment = postSubCommentRepository.findById(postSubCommentId)
                .orElseThrow(() -> new NoResultException("PostSubCommentServiceImpl - updatePostSubComment : 댓글 없음"));
        // caller 와 해당 게시물의 user_id가 같을 때만 삭제
        if ( caller.getId() == findPostSubComment.getWriter().getId() ){
            findPostSubComment.delete();
        }
        else {
            throw new IllegalCallerException("PostSubCommentServiceImpl - updatePostSubComment : 권한 없음");
        }
    }
}
