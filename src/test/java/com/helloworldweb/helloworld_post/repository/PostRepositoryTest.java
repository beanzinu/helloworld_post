package com.helloworldweb.helloworld_post.repository;
import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.PostComment;
import com.helloworldweb.helloworld_post.domain.PostSubComment;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.service.PostService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    // cache 테스트
//    @Autowired
    @Resource(name = "postServiceImpl")
    PostService postService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        User writer = User.builder()
                .email("123")
                .build();
        userRepository.save(writer);

        PostSubComment postSubComment = PostSubComment.builder()
                .writer(writer)
                .content("123")
                .build();
        PostSubComment postSubComment2 = PostSubComment.builder()
                .writer(writer)
                .content("123")
                .build();
        PostSubComment postSubComment3 = PostSubComment.builder()
                .writer(writer)
                .content("123")
                .build();

        PostComment postComment = PostComment.builder()
                .build();
        postComment.addPostSubComment(postSubComment);
        postComment.addPostSubComment(postSubComment2);
        postComment.addPostSubComment(postSubComment3);

        PostSubComment postSubComment4 = PostSubComment.builder()
                .writer(writer)
                .content("123")
                .build();
        PostSubComment postSubComment5 = PostSubComment.builder()
                .writer(writer)
                .content("123")
                .build();
        PostSubComment postSubComment6 = PostSubComment.builder()
                .writer(writer)
                .content("123")
                .build();

        PostComment postComment1 = PostComment.builder().build();
        postComment1.addPostSubComment(postSubComment4);
        postComment1.addPostSubComment(postSubComment5);
        postComment1.addPostSubComment(postSubComment6);

        User postWriter = User.builder().email("234").build();
        userRepository.save(postWriter);
        Post post = Post.builder()
                .user(postWriter)
                .build();
        post.addPostComment(postComment);
        post.addPostComment(postComment1);

        // Page 확인을 위해 10개의 Post 저장
        for(int i=0;i<10;i++){
            Post post1 = Post.builder()
                    .user(postWriter)
                    .views(Integer.toUnsignedLong(i))
                    .build();
            postRepository.save(post1);
        }

        // 100개의 PostComment 생성
//        for (int i=0;i<100;i++){
//            PostComment pc = PostComment.builder()
//                    .build();
//            post.addPostComment(pc);
//        }

        postRepository.save(post);
        System.out.println("START");
        System.out.println("======================");
    }
    @AfterEach
    void fin(){
        System.out.println("======================");
        System.out.println("FIN");

    }

    @Test
    void findAllByUserId(){
        User user = userRepository.findByEmail("234").get();
        List<Post> allByUserId = postRepository.findAllByUserId(user.getId());
    }

    @Test
    void findAllWithUser(){
        postRepository.findAllWithUser();
    }

    @Test
    @DisplayName("페이지별 모든 게시물 조회")
    void findAllByPage(){
        for (int i=0;i<5;i++) {
            System.out.println("i = " + i);
            Pageable pageable = PageRequest.of(i, 3);
            Page<Post> all = postRepository.findAll(pageable);
            List<Post> collect = all.toList();
            for (Post p : collect) {
                System.out.println("p.getId() = " + p.getId());
            }
            System.out.println("=============== = ");
        }
    }


    @Test
    @DisplayName("LRU 알고리즘을 통한 캐시")
    void findPostWithCache(){
        // [12,13,14,15,16]
        for(int i=0;i<5;i++){
            PostRequestDto p =PostRequestDto.builder()
                    .content(Integer.toString(i))
                    .build();
            postService.addPost(p,1L);
        }
        // [14,15,16,12,13]
        postService.getPost(12L);
        postService.getPost(13L);
        // [ 16,12,13,17,18]
        for(int i=0;i<2;i++){
            PostRequestDto p =PostRequestDto.builder()
                    .content(Integer.toString(i))
                    .build();
            postService.addPost(p,1L);
        }
    }

    @Test
    @DisplayName("페이지 조회 시 캐시사용")
    void cacheResultsFromFindAllByPage(){
        Pageable pageable = PageRequest.of(0,3);
        Thread thread = new Thread(new MyRunnable());
        thread.start();
        postService.getAllPostByPage(pageable);

    }
    
    @Test
    @DisplayName("변경된 내용 캐시로부터 가져오기")
    void getChangedResultFromCache(){
        PostResponseDto beforePost = postService.getPost(1L);
        System.out.println("beforePost.getTitle() = " + beforePost.getTitle());
        postService.updatePost(PostRequestDto.builder().post_id(1L).title("cache title").user_id(2L).build(),2L);
        PostResponseDto afterPost = postService.getPost(1L);
        System.out.println("afterPost.getTitle() = " + afterPost.getTitle());
    }
    

    @Test
    @DisplayName("병합을 통한 임의수정")
    void changePostByMerging(){
        Post post = Post.builder()
                .id(1L)
                .title("merge title")
//                .content("merge content")
                .build();
        postRepository.save(post);
    }

    @Nested
    class MyRunnable implements Runnable{
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            Pageable pageable1 = PageRequest.of(1,3);
            System.out.println("Thread working");
            postService.getAllPostByPage(pageable1);
        }
    }

    @Test
    @DisplayName("상위 5개 질문")
    void getTop5Questions(){
        List<Post> top5ByViewsDesc = postRepository.findTop5ByOrderByViewsDesc();
        for ( Post p : top5ByViewsDesc){
            System.out.println("p.getViews() = " + p.getViews());
        }
    }

}

