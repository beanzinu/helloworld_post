package com.helloworldweb.helloworld_post.repository;

import com.helloworldweb.helloworld_post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    /**
     * 유저 아이디를 통해 유저가 작성한 모든 게시물 조회
     * @param id : 유저 PK
     * @return : List<Post>
     */
    List<Post> findAllByUserId(@Param(value = "id") Long id);

    /**
     * 모든 게시물 조회 시 유저정보 포함
     * @return : List<Post>
     */
    @Query(value = "select p from Post p join fetch p.user")
    List<Post> findAllWithUser();

    @Query(value = "select p from Post p join fetch p.user")
    List<Post> findAllWithUser(Pageable pageable);

}
