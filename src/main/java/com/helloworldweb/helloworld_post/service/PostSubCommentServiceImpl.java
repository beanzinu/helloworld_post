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
}
