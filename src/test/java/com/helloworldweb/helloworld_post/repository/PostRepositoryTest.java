package com.helloworldweb.helloworld_post.repository;
import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.PostComment;
import com.helloworldweb.helloworld_post.domain.PostSubComment;
import com.helloworldweb.helloworld_post.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

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

}
