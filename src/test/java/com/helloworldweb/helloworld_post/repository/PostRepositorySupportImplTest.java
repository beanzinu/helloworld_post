package com.helloworldweb.helloworld_post.repository;

import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.repository.querydsl.PostRepositorySupport;
import com.helloworldweb.helloworld_post.repository.querydsl.PostRepositorySupportImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class PostRepositorySupportImplTest {

    ;
    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void setup(){
        Post p1 = Post.builder().title("react and native").build();
        Post p2 = Post.builder().title("react").tags("react").build();
        Post p3 = Post.builder().title("123123").tags("react").build();
        postRepository.save(p1);
        postRepository.save(p2);
        postRepository.save(p3);
    }

    @Test
    void test(){
        Pageable pageable = PageRequest.of(0,10);
        PageImpl<Post> searchedPostListWithPage = postRepository.findPostListWithPageAndSentence("\"react and native\"", pageable);
        System.out.println("searchedPostListWithPage = " + searchedPostListWithPage.getContent().size());
    }
    
    @Test
    void tagTest(){
        Pageable pageable = PageRequest.of(0,10);
        PageImpl<Post> searchedPostListWithPage = postRepository.findPostListWithPageAndSentence("%react%", pageable);
        System.out.println("searchedPostListWithPage = " + searchedPostListWithPage.getContent().size());
        System.out.println("searchedPostListWithPage.getContent().get(0).getTitle() = " + searchedPostListWithPage.getContent().get(0).getTitle());
    }

    @Test
    void phraseTest(){
        Pageable pageable = PageRequest.of(0,10);
        PageImpl<Post> searchedPostListWithPage = postRepository.findPostListWithPageAndSentence("react", pageable);
        System.out.println("searchedPostListWithPage = " + searchedPostListWithPage.getContent().size());
    }

}
