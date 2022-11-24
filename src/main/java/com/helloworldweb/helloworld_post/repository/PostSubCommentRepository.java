package com.helloworldweb.helloworld_post.repository;

import com.helloworldweb.helloworld_post.domain.PostSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostSubCommentRepository extends JpaRepository<PostSubComment,Long> {

//    @EntityGraph(attributePaths = {"writer"},type = EntityGraph.EntityGraphType.LOAD)
//    @Query(value="select psc from PostSubComment psc")
    @Query(value = "select distinct psc,w from PostSubComment psc " +
            "left outer join psc.postComment.post " +
            "left outer join psc.writer w " +
            "where psc.writer.id=:userId"
            )
    List<PostSubComment> getAllPostSubCommentWithPostByWriterId(@Param(value = "userId")Long userId);


    @Query(value = "select psc from PostSubComment psc " +
            "join fetch psc.postComment pc " +
            "join fetch pc.post p " +
            "where psc.writer.id=:userId"

    )
    List<PostSubComment> findAllByWriterId(@Param(value = "userId") Long userId);
}
