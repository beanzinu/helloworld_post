package com.helloworldweb.helloworld_post.jwt;

import com.helloworldweb.helloworld_post.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final long TOKEN_VALID_TIME = 1000L * 60 * 60 * 10; //10시간

    @Value("${secret.jwt.key}")
    private String secretKey;


    public Authentication getAuthentication(String token){
        UserDetails userDetails =  (UserDetails) User.builder().id(Long.parseLong(getUserIdByToken(token)));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    // Jwt 안의 유저 PK를 가져옴.
    public String getUserIdByToken(String token)
    {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean verifyToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            long valid_time = claims.getBody().getExpiration().getTime() - claims.getBody().getIssuedAt().getTime();
            // refresh_token인지 확인
            return valid_time > TOKEN_VALID_TIME ? false : !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
