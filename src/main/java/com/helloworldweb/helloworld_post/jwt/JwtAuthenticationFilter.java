package com.helloworldweb.helloworld_post.jwt;

import com.helloworldweb.helloworld_post.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader("Auth");
        response.setCharacterEncoding("UTF-8");

        // CORS 정책
        ( (HttpServletResponse) response ).setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        ( (HttpServletResponse) response ).addHeader("Access-Control-Allow-Headers","Auth,Content-Type");
        ( (HttpServletResponse) response ).addHeader("Access-Control-Allow-Methods","PUT,OPTIONS,DELETE");

        // 임시
        System.out.println("===== 임시로 필터통과 ======");
        User serverUser = User.builder()
                .id(1L)
                .email("test@email.com")
                .build();
        UsernamePasswordAuthenticationToken serverToken = new UsernamePasswordAuthenticationToken(serverUser, "", serverUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(serverToken);
        chain.doFilter(request,response);


        // 토큰 유효성 검증 및 Authentication 객체 생성 후 SecurityContextHolder 에 등록.
//        if (token != null && jwtTokenProvider.verifyToken(token)) {
//            Authentication auth = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//        chain.doFilter(request, response);

    }
}

