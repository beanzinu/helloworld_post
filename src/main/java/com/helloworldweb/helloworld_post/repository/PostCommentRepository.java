package com.helloworldweb.helloworld_post.repository;

import com.helloworldweb.helloworld_post.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {

    @Query(value = "select distinct pc from PostComment pc "+
            "join fetch pc.post " +
            "join fetch pc.postSubComments psc " +
            "where pc.post.id=:postId"
    )
    List<PostComment> findAllPostCommentByPostId(@Param(value = "postId") Long postId);
}
