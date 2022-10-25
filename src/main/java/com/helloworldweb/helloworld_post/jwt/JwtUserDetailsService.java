package com.helloworldweb.helloworld_post.jwt;

import com.helloworldweb.helloworld_post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService{

    private final UserRepository userRepository;

    public UserDetails loadUserByUserId(Long userId){
        return userRepository.findById(userId).orElseThrow(NoResultException::new);
    }

}
