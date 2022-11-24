package com.helloworldweb.helloworld_post.repository.querydsl;

import com.helloworldweb.helloworld_post.domain.Post;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface PostRepositorySupport{
    public PageImpl<Post> findPostListWithPageAndSentence(String sentence, Pageable pageable);
}
