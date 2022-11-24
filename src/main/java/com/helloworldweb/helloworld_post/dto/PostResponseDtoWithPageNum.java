package com.helloworldweb.helloworld_post.dto;

import com.helloworldweb.helloworld_post.domain.Post;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostResponseDtoWithPageNum {
    private int pageNum;
    private List<PostResponseDto> postResponseDtoList;

    @Builder
    public PostResponseDtoWithPageNum(int pageNum, List<PostResponseDto> postResponseDtoList) {
        this.pageNum = pageNum;
        this.postResponseDtoList = postResponseDtoList;
    }

    public PostResponseDtoWithPageNum(Page<Post> page){
        this.pageNum = page.getTotalPages();
        this.postResponseDtoList = page.getContent()
                .stream().map(PostResponseDto::getDtoWithUser)
                .collect(Collectors.toList());
    }
}
