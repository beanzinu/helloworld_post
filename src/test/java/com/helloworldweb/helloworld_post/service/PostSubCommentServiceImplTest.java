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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostSubCommentServiceImplTest {
    @Mock
    UserService userService;
    @Mock
    PostRepository postRepository;
    @Mock
    PostCommentRepository postCommentRepository;
    @Mock
    PostSubCommentRepository postSubCommentRepository;
    @InjectMocks
    PostSubCommentServiceImpl postSubCommentService;

    private static User testCaller = User.builder().id(1L).email("123").build();
    private static Post testPost = Post.builder().id(2L).build();
    private static PostComment testPostComment = PostComment.builder().id(3L).post(testPost).build();
    private static PostSubComment testPostSubComment = PostSubComment.builder()
            .id(4L)
            .postComment(testPostComment)
            .writer(testCaller)
            .content("댓글 내용")
            .build();
    @Test
    @DisplayName("댓글들 조회")
    void getPostSubCommentList(){
        // given
        testPostComment.addPostSubComment(testPostSubComment);
        when(postCommentRepository.findAllPostCommentByPostId(any(Long.class))).thenReturn(List.of(testPostComment));

        // when
        List<PostCommentResponseDto> postCommentResponseDtoList = postSubCommentService.getPostCommentListByPostId(2L);

        // then
        assertEquals(postCommentResponseDtoList.get(0).getPostSubCommentResponseDtoList().get(0).getContent()
                ,testPostSubComment.getContent());
        assertEquals(postCommentResponseDtoList.get(0).getPostSubCommentResponseDtoList().get(0).getUserResponseDto().getEmail()
                ,testCaller.getEmail());

    }


    @Test
    @DisplayName("첫 댓글을 단 경우")
    void addPostSubComment_1(){
        // given
        PostSubCommentRequestDto postSubCommentRequestDto = PostSubCommentRequestDto.builder().content("댓글 내용").build();
        when(userService.getCaller()).thenReturn(testCaller);
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(testPost));

        // when
        PostSubCommentResponseDto postSubCommentResponseDto = postSubCommentService.addPostSubComment(testPost.getId(),null,postSubCommentRequestDto);

        // then
        assertEquals(postSubCommentResponseDto.getUserResponseDto().getEmail(),"123");
        assertEquals(postSubCommentResponseDto.getContent(),"댓글 내용");
    }

    @Test
    @DisplayName("대댓글을 단 경우")
    void addPostSubComment_2(){
        // given
        PostSubCommentRequestDto postSubCommentRequestDto = PostSubCommentRequestDto.builder().content("댓글 내용").build();
        when(userService.getCaller()).thenReturn(testCaller);
        when(postCommentRepository.findById(any(Long.class))).thenReturn(Optional.of(testPostComment));

        // when
        PostSubCommentResponseDto postSubCommentResponseDto = postSubCommentService.addPostSubComment(testPost.getId(),testPostComment.getId(),postSubCommentRequestDto);

        // then
        assertEquals(postSubCommentResponseDto.getUserResponseDto().getEmail(),"123");
        assertEquals(postSubCommentResponseDto.getContent(),"댓글 내용");
    }

}
