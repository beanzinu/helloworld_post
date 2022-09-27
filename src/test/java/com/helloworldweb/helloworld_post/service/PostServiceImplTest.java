package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @Mock
    UserServiceImpl userService;
    @InjectMocks
    PostServiceImpl postService;

    private static final String testUserEmail = "test@email.com";
    private static final String testTitle = "질문 제목1";
    private static final String testContent = "질문 내용1";
    private static final Long testUserId = 1L;
    private static final Long testPostId = 2L;
    private static final User testUser = User.builder()
            .id(testUserId)
            .email(testUserEmail)
            .build();
    private static final Post testPost = Post.builder()
            .id(testPostId)
            .title(testTitle)
            .content(testContent)
            .user(testUser)
            .build();
    private static final PostRequestDto testPostRequestDto = PostRequestDto.builder()
            .user_id(testUserId)
            .post_id(testPostId)
            .title(testTitle)
            .content(testContent)
            .build();

    @Test
    @DisplayName("게시물 작성")
    void addPost(){
    //given
        when(userService.getUserByEmail(any(String.class))).thenReturn(testUser);
        when(postRepository.save(any(Post.class))).then(AdditionalAnswers.returnsFirstArg());

    //when
        PostResponseDto postResponseDto = postService.addPost(testPostRequestDto,testUserEmail);

    //then >> 제목과 내용이
        assertEquals(postResponseDto.getContent(),testPostRequestDto.getContent());
        assertEquals(postResponseDto.getTitle(),testPostRequestDto.getTitle());
        assertEquals(postResponseDto.getUser_id(),testPostRequestDto.getUser_id());

    }

    @Test
    @DisplayName("게시물 조회")
    void getPost(){
    //given
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(testPost));
    //when
        PostResponseDto postResponseDto = postService.getPost(testPostId);
    //then
        assertEquals(postResponseDto.getPost_id(),testPostId);
    }

    @Test
    @DisplayName("게시물 수정")
    void updatePost(){
    //given
        // 변경전 Post 생성
        Post beforeUpdatePost = Post.builder()
                .id(testPostId)
                .user(testUser)
                .title("변경전 제목")
                .content("변경전 내용")
                .build();
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeUpdatePost));
        when(userService.getUserByEmail(any(String.class))).thenReturn(testUser);
    //when
        PostResponseDto postResponseDto = postService.updatePost(testPostRequestDto, testUserEmail);

    //then
        assertEquals(postResponseDto.getPost_id(),testPostId);
        assertEquals(postResponseDto.getTitle(),testTitle);
        assertEquals(postResponseDto.getContent(),testContent);
        assertEquals(postResponseDto.getUser_id(),testUserId);
    }

    @Test
    @DisplayName("주인이 아닌 사용자의 게시글 수정")
    void updatePostByIllegalCaller(){
    //given
        // 변경전 Post 생성
        String illegallCallerEmail = "illegalcaller@email.com";
        User illegaCaller = User.builder()
                .id(-1L)
                .email(illegallCallerEmail)
                .build();
        Post beforeUpdatePost = Post.builder()
                .id(testPostId)
                .user(testUser)
                .title("변경전 제목")
                .content("변경전 내용")
                .build();
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeUpdatePost));
        when(userService.getUserByEmail(any(String.class))).thenReturn(illegaCaller);
    //then
        assertThrows(IllegalCallerException.class,()->{
            postService.updatePost(testPostRequestDto,illegallCallerEmail);
        });
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost(){
    //given
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(testPost));
        when(userService.getUserByEmail(any(String.class))).thenReturn(testUser);
    //when
    //then
        assertDoesNotThrow(()->{
            postService.deletePost(testPostId,testUserEmail);
        });
    }




}
