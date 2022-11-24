package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.User;
import com.helloworldweb.helloworld_post.dto.UserRequestDto;

public interface UserService {
    /**
     * READ : 이메일을 통한 유저조회
     * @param email : 이메일
     * @return : User
     */
    User getUserByEmail(String email);

    /**
     * READ : Id를 통한 유저조회
     * @param id : 유저 PK
     * @return : User
     */
    User getUserById(Long id);

    /**
     * JWT 토큰 내의 유저정보로 유저조회
     * @return : User
     */
    User getCaller();
}
