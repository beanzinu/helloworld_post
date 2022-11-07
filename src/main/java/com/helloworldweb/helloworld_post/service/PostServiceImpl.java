package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.Cache;
import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.PostRequestDto;
import com.helloworldweb.helloworld_post.dto.PostResponseDto;
import com.helloworldweb.helloworld_post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    
    private final PostRepository postRepository;
    private final UserService userService;

    // 캐시
    private static Cache<Long,Post> postCache = new Cache<>(5);

    /**
     * CREATE : 게시물 작성
     * @param postRequestDto : 클라이언트로부터 받은 정보
     * @param userId : 유저의 PK
     * @return : PostResponseDto
    **/
    @Override
    @Transactional
    public PostResponseDto addPost(PostRequestDto postRequestDto,Long userId) {
        // Dto -> Post 객체
        Post newPost = postRequestDto.toEntity();
        // 어떤 유저가 작성했는지에 대한 연관관계 설정
        User caller = userService.getUserById(userId);
        newPost.changeUser(caller);
        // DB
        Post savedPost = postRepository.save(newPost);
        return new PostResponseDto(savedPost);
    }

    /**
     * READ : 게시물 조회
     * @param postId : 게시물의 PK
     * @return : PostResponseDto
     */
    @Override
    public PostResponseDto getPost(Long postId) {
        if ( postCache.containsKey(postId)) { // 캐시 hit
            Post cachePost = postCache.get(postId);
            return new PostResponseDto(cachePost);
        }
        else { // 캐시 miss
            Post findPost = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
            // 캐시
            PostResponseDto postResponseDto = PostResponseDto.getDtoWithUser(findPost);
            postCache.put(findPost.getId(), findPost);
            return postResponseDto;
        }
    }

    /**
     * UPDATE : 게시물 수정
     * @param postRequestDto : 게시물의 내용
     * @param userId : 유저의 PK
     * @return : PostResponseDto
     */
    @Override
    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long userId) {
        // RequestDto 안의 postId를 통해 Post 조회
        Long targetPostId = postRequestDto.getPost_id();
        Post findPost = postRepository.findById(targetPostId).orElseThrow(NoSuchElementException::new);
        // email 을 통해 찾은 유저의 글이 아닌 경우 IllegalCallerException
        User caller = userService.getUserById(userId);
        if (findPost.getUser().getId() != caller.getId())
            throw new IllegalCallerException();
        // findPost 안의 내용을 postRequestDto 안의 내용으로 대체 : Dirty Check
        Post changedPost = findPost.changeWithDto(postRequestDto);
        // 캐시
        if ( postCache.containsKey(changedPost.getId())){
            postCache.put(changedPost.getId(),changedPost);
        }
        return new PostResponseDto(changedPost);
    }

    /**
     * DELETE : 게시물 삭제
     * @param postId : 게시물의 PK
     * @param userId  : 유저의 PK
     * @return : Boolean
     */
    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        // postId를 통해 Post 조회
        Post findPost = postRepository.findById(postId).orElseThrow(NoResultException::new);
        // email 을 통해 User 조회 -> 해당 유저의 글이 아닌 경우 IllegalCallerException
        User caller = userService.getUserById(userId);
        if (findPost.getUser().getId() != caller.getId())
            throw new IllegalCallerException();

        postRepository.delete(findPost);
        // 캐시
        postCache.remove(postId);
    }

    /**
     * READ : 해당 이메일의 유저가 작성한 모든 게시물 조회
     * @param userId : 유저의 PK
     * @return : List<PostResponseDto>
     */
    @Override
    public List<PostResponseDto> getAllPostByUserId(Long userId) {
        User findUser = userService.getUserById(userId);
        return postRepository.findAllByUserId(findUser.getId())
                .stream().map(PostResponseDto::getDtoWithUser)
                .collect(Collectors.toList());
    }

    /**
     * READ : 해당 페이지의 모든 게시물 조회
     * @param pageable : 페이지와 사이즈를 담고있는 Pageable 객체
     * @return : List<PostResponseDto>
     */
    @Override
    public List<PostResponseDto> getAllPostByPage(Pageable pageable) {
        List<Post> findPosts = postRepository.findAllWithUser(pageable);
        // 캐시
        Map<Long,Post> map = new HashMap<>();
        for ( Post p : findPosts){
            map.put(p.getId(),p);
        }
        postCache.syncPutAll(map);

        return findPosts.stream().map(PostResponseDto::getDtoWithUser)
                .collect(Collectors.toList());
    }

    /**
     * READ : 상위 질문들 X개 조회
     * @return : List<PostResponseDto>
     */
    @Override
    public List<PostResponseDto> getTopQuestions() {
        List<Post> findPosts = postRepository.findTop5ByOrderByViewsDesc();
        return findPosts.stream().map(PostResponseDto::new)
                .collect(Collectors.toList());
    }
}
