package com.helloworldweb.helloworld_post.repository;

import com.helloworldweb.helloworld_post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
