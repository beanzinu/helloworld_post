package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.UserRequestDto;
import com.helloworldweb.helloworld_post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    /**
     * 이메일을 통한 유저조회
     * @param email : 이메일
     * @return : User 객체
     */
    @Override
    public User getUserByEmail(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        return findUser;
    }

    public void addUser(UserRequestDto userRequestDto){
        userRepository.save(userRequestDto.toEntity());
    }

}
