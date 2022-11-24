package com.helloworldweb.helloworld_post.controller;

import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.repository.UserRepository;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.Math.random;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        User writer = User.builder()
                .id(1L)
                .email("123")
                .build();
        userRepository.save(writer);
    }

    @Test
    public void test() throws Exception {
        // Given & When

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "1");
        jsonObject.put("email", "test@email.com");
        jsonObject.put("profileUrl", "123123");
        // Then


        this.mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toJSONString()))
                .andDo(print())
                .andExpect(status().isOk());

//        for(int i=0;i<3;i++){
//            Thread thread = new Thread(new A());
//            thread.start();
//        }
        System.out.println("fin");

    }

    @Nested
    class A implements Runnable{
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        @Autowired
        private MockMvc mockMvc;
        @Override
        public void run() {
            // Given & When
            int i = (int) random() * 1000;
            User writer = User.builder()
                    .id(Integer.toUnsignedLong(i))
                    .email("123")
                    .build();
            userRepository.save(writer);
            String s = Integer.toString(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",s);
            jsonObject.put("email", "test@email.com");
            jsonObject.put("profileUrl", "123123");
            // Then

            try {
                this.mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString()))
                        .andDo(print())
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
