package com.helloworldweb.helloworld_post.repository;

import com.helloworldweb.helloworld_post.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {
}
