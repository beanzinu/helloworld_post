package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDtoWithPageNum;
import com.helloworldweb.helloworld_post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
        when(userService.getUserById(any(Long.class))).thenReturn(testUser);
        when(postRepository.save(any(Post.class))).then(AdditionalAnswers.returnsFirstArg());

    //when
        PostResponseDto postResponseDto = postService.addPost(testPostRequestDto,testUserId);

    //then >> 제목과 내용이
        assertEquals(testPostRequestDto.getContent(),postResponseDto.getContent());
        assertEquals(testPostRequestDto.getTitle(),postResponseDto.getTitle());
        assertEquals(testPostRequestDto.getUser_id(),postResponseDto.getUser_id());

    }

    @Test
    @DisplayName("게시물 조회")
    void getPost(){
    //given
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(testPost));
    //when
        PostResponseDto postResponseDto = postService.getPost(testPostId);
    //then
        assertEquals(testPostId,postResponseDto.getPost_id());
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
        when(userService.getUserById(any(Long.class))).thenReturn(testUser);
    //when
        PostResponseDto postResponseDto = postService.updatePost(testPostRequestDto, testUserId);

    //then
        assertEquals(testPostId,postResponseDto.getPost_id());
        assertEquals(testTitle,postResponseDto.getTitle());
        assertEquals(testContent,postResponseDto.getContent());
        assertEquals(testUserId,postResponseDto.getUser_id());
    }

    @Test
    @DisplayName("주인이 아닌 사용자의 게시글 수정")
    void updatePostByIllegalCaller(){
    //given
        // 변경전 Post 생성
        User illegalCaller = User.builder()
                .id(-1L)
                .build();
        Post beforeUpdatePost = Post.builder()
                .id(testPostId)
                .user(testUser)
                .title("변경전 제목")
                .content("변경전 내용")
                .build();
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeUpdatePost));
        when(userService.getUserById(any(Long.class))).thenReturn(illegalCaller);
    //then
        assertThrows(IllegalCallerException.class,()->{
            postService.updatePost(testPostRequestDto,-1L);
        });
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost(){
    //given
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(testPost));
        when(userService.getUserById(any(Long.class))).thenReturn(testUser);
    //when
    //then
        assertDoesNotThrow(()->{
            postService.deletePost(testPostId,1L);
        });
    }

    @Test
    @DisplayName("특정 유저의 게시물 조회")
    void getAllPostByUserId(){
    //given
        when(postRepository.findAllByUserId(any(Long.class))).thenReturn(List.of(testPost));
        when(userService.getUserById(any(Long.class))).thenReturn(testUser);
    //when
        List<PostResponseDto> findPosts = postService.getAllPostByUserId(testUser.getId());
    //then
        assertEquals(testPost.getContent(),findPosts.get(0).getContent());
        assertEquals(testPost.getTitle(),findPosts.get(0).getTitle());
        assertEquals(testUser.getEmail(),findPosts.get(0).getUserResponseDto().getEmail());
    }

    @Test
    @DisplayName("해당 페이지의 모든 게시물 조회")
    void getAllPostByPage(){
    //given : 총 4개의 게시물이 있을 경우
        Post post1 = Post.builder().user(testUser).build();
        Post post2 = Post.builder().user(testUser).build();
        Post post3 = Post.builder().user(testUser).build();
        PageImpl<Post> savedPage = new PageImpl<>(List.of(testPost, post1, post2, post3));
        Page<Post> testPage = new PageImpl<>(List.of(testPost,post1,post2));
//        List<Post> testPage = List.of(testPost,post1,post2);
        when(postRepository.findAll(any(Pageable.class))).thenReturn(testPage);
    //when : 첫번째 페이지의 게시물을 찾는다.
        Pageable pageable = PageRequest.of(0,3);
        PostResponseDtoWithPageNum findPostDto = postService.getAllPostByPage(pageable);
        List<PostResponseDto> findPostDtos = findPostDto.getPostResponseDtoList();
        //then : 3개의 게시물만 찾아져야한다.
        assertNotEquals(savedPage.getTotalPages(),findPostDtos.size());
        assertEquals(3,findPostDtos.size());
        assertEquals(testPost.getContent(),findPostDtos.get(0).getContent());
        assertEquals(testPost.getTitle(),findPostDtos.get(0).getTitle());
        assertEquals(testUser.getEmail(),findPostDtos.get(0).getUserResponseDto().getEmail());
    }

    @Test
    @DisplayName("상위 X개 게시물 검색")
    void getTopQuestions(){
    //given
        Post post1 = Post.builder().user(testUser).views(5L).build();
        Post post2 = Post.builder().user(testUser).views(4L).build();
        Post post3 = Post.builder().user(testUser).views(3L).build();
        Post post4 = Post.builder().user(testUser).views(2L).build();
        Post post5 = Post.builder().user(testUser).views(1L).build();
        List<Post> postList = List.of(post1,post2,post3,post4,post5);
        when(postRepository.findTop5ByOrderByViewsDesc()).thenReturn(postList);
    //when
        List<PostResponseDto> findPostDtoList = postService.getTopQuestions();
    //then
        assertEquals(5L,findPostDtoList.get(0).getViews());
        assertEquals(4L,findPostDtoList.get(1).getViews());
        assertEquals(3L,findPostDtoList.get(2).getViews());
        assertEquals(2L,findPostDtoList.get(3).getViews());
        assertEquals(1L,findPostDtoList.get(4).getViews());

    }


}
