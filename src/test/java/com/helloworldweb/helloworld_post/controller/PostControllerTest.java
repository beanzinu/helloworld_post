package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.repository.PostRepository;
import com.helloworldweb.helloworld_post.repository.UserRepository;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;


    private final String testTitle = "test title";
    private final String testContent = "test content";

    @Test
    @DisplayName("게시물 등록")
    public void registerPost() throws Exception {
        // Given & When
        User testUser = User.builder().id(1L).email("test@email.com").build();
        userRepository.save(testUser);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title",testTitle);
        jsonObject.put("content", testContent);
        // Then

        mockMvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toJSONString()))
                .andDo(print())
                .andExpect(jsonPath("$.data.title").value(testTitle))
                .andExpect(jsonPath("$.data.content").value(testContent))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 조회")
    public void getPost() throws Exception {
        // Given & When
        User testUser = User.builder().id(1L).email("test@email.com").build();
        userRepository.save(testUser);
        Post testPost = Post.builder().id(1L).title(testTitle).content(testContent).user(testUser).build();
        postRepository.save(testPost);

        // Then
        mockMvc.perform(get("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id","1"))
                .andDo(print())
                .andExpect(jsonPath("$.data.title").value(testTitle))
                .andExpect(jsonPath("$.data.content").value(testContent))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("특정 유저의 질문 검색")
    public void getPostByUserId() throws Exception {
        // Given & When
        User testUser = User.builder().id(1L).email("test@email.com").build();
        userRepository.save(testUser);
        Post testPost = Post.builder().id(1L).title(testTitle).content(testContent).user(testUser).build();
        postRepository.save(testPost);

        // Then
        mockMvc.perform(get("/api/post/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id","1"))
                .andDo(print())
                .andExpect(jsonPath("$.data[0].title").value(testTitle))
                .andExpect(jsonPath("$.data[0].content").value(testContent))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("해당 페이지의 질문 조회")
    public void getPostByPage() throws Exception {
        // Given & When
        User testUser = User.builder().id(1L).email("test@email.com").build();
        userRepository.save(testUser);
        for(long i=0;i<20;i++){
            Post testPost = Post.builder().id(i).title(testTitle).content(testContent).user(testUser).build();
            postRepository.save(testPost);
        }

        // Then
        mockMvc.perform(get("/api/post/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page","0"))
                .andDo(print())
                .andExpect(jsonPath("$.data[0].title").value(testTitle))
                .andExpect(jsonPath("$.data[0].content").value(testContent))
                .andExpect(jsonPath("$.data[10]").doesNotExist())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("상위 X개 질문 조회")
    public void getTopQuestions() throws Exception {
        // Given & When
        User testUser = User.builder().id(1L).email("test@email.com").build();
        userRepository.save(testUser);
        for(long i=0;i<10;i++){
            Post testPost = Post.builder().id(i).title(testTitle).content(testContent).user(testUser).build();
            postRepository.save(testPost);
        }

        // Then
        mockMvc.perform(get("/api/post/top-questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data[0].title").value(testTitle))
                .andExpect(jsonPath("$.data[0].content").value(testContent))
                .andExpect(jsonPath("$.data[5]").doesNotExist())
                .andExpect(status().isOk());
    }


}
