package com.helloworldweb.helloworld_post.service;

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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    
    private final PostRepository postRepository;
    private final UserService userService;

    /**
     * CREATE : 게시물 작성
     * @param postRequestDto : 클라이언트로부터 받은 정보
     * @param email : 쿠키 안의 이메일
     * @return : PostResponseDto
    **/
    @Override
    @Transactional
    public PostResponseDto addPost(PostRequestDto postRequestDto, String email) {
        // Dto -> Post 객체
        Post newPost = postRequestDto.toEntity();
        // 어떤 유저가 작성했는지에 대한 연관관계 설정
        User caller = userService.getUserByEmail(email);
        Post userMappedPost = newPost.changeUser(caller);

        Post savedPost = postRepository.save(userMappedPost);
        return new PostResponseDto(savedPost);
    }

    /**
     * READ : 게시물 조회
     * @param postId : 게시물의 PK
     * @return : PostResponseDto
     */
    @Override
    public PostResponseDto getPost(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
        return new PostResponseDto(findPost);
    }

    /**
     * UPDATE : 게시물 수정
     * @param postRequestDto : 게시물의 내용
     * @param email          : 쿠키 안의 이메일
     * @return : PostResponseDto
     */
    @Override
    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto, String email) {
        // RequestDto 안의 postId를 통해 Post 조회
        Long targetPostId = postRequestDto.getPost_id();
        Post findPost = postRepository.findById(targetPostId).orElseThrow(NoSuchElementException::new);
        // email 을 통해 찾은 유저의 글이 아닌 경우 IllegalCallerException
        User caller = userService.getUserByEmail(email);
        if (findPost.getUser().getId() != caller.getId())
            throw new IllegalCallerException();
        // findPost 안의 내용을 postRequestDto 안의 내용으로 대체
        Post changedPost = findPost.changeWithDto(postRequestDto);
        return new PostResponseDto(changedPost);
    }

    /**
     * DELETE : 게시물 삭제
     * @param postId : 게시물의 PK
     * @param email  : 쿠키 안의 이메일
     * @return : Boolean
     */
    @Override
    @Transactional
    public void deletePost(Long postId, String email) {
        // postId를 통해 Post 조회
        Post findPost = postRepository.findById(postId).orElseThrow(NoResultException::new);
        // email 을 통해 User 조회 -> 해당 유저의 글이 아닌 경우 IllegalCallerException
        User caller = userService.getUserByEmail(email);
        if (findPost.getUser().getId() != caller.getId())
            throw new IllegalCallerException();

        postRepository.delete(findPost);
    }

    /**
     * READ : 해당 이메일의 유저가 작성한 모든 게시물 조회
     * @param userId : 유저의 이메일
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
        return postRepository.findAll(pageable).toList()
                .stream().map(PostResponseDto::getDtoWithUser)
                .collect(Collectors.toList());
    }
}
