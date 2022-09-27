package com.helloworldweb.helloworld_post.service;

import com.helloworldweb.helloworld_post.domain.User;

public interface UserService {
    /**
     * 이메일을 통한 유저조회
     * @param email : 이메일
     * @return : User 객체
     */
    User getUserByEmail(String email);

}
