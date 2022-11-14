package com.helloworldweb.helloworld_post.repository.querydsl;

import com.helloworldweb.helloworld_post.domain.Post;
import com.helloworldweb.helloworld_post.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositorySupportImpl extends QuerydslRepositorySupport implements PostRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPost post = QPost.post;

    public PostRepositorySupportImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private void addTitleAndContentContainsInBooleanBuilder(BooleanBuilder builder,String s){
        builder.or(post.title.contains(s));
        builder.or(post.content.contains(s));
    }
    private void addTagContainsInBooleanBuilder(BooleanBuilder builder,String s){
        builder.or(post.tags.contains(s));
    }

    public PageImpl<Post> findPostListWithPageAndSentence(String sentence, Pageable pageable){
        BooleanBuilder searchBuilder = new BooleanBuilder();

        // 검색문장 구분
        // "react and native" [tag] phrase
        boolean exactPhraseFlag = false;
        String exactPhraseConcat = "";
        for(String s : sentence.split(" ")){
            // 1. 태그 검색 : [tag]
            if( s.startsWith("%") && s.endsWith("%") ) {
                // [tag] -> tag
                addTagContainsInBooleanBuilder(searchBuilder,s.substring(1,s.length()-1));
            }
            // 2. 정확한 문구검색 : "an exact phrase"
            else if( s.startsWith("\"")){
                // "react"
                if( s.endsWith("\"")){
                    addTitleAndContentContainsInBooleanBuilder(searchBuilder,s.substring(1,s.length()-1));
                }
                // "react and native" -> "react
                else{
                    exactPhraseFlag = true;
                    exactPhraseConcat += s.substring(1);
                }
            }
            // "react and native" -> native"
            else if( s.endsWith("\"") ){
                exactPhraseFlag = false;
                exactPhraseConcat += " " + s.substring(0,s.length()-1);
                addTitleAndContentContainsInBooleanBuilder(searchBuilder,exactPhraseConcat);
                exactPhraseConcat = "";
            }
            // "react and native" -> and
            else if( exactPhraseFlag ){
                exactPhraseConcat += " " + s;
            }
            // 3. 일반 검색 : phrase
            else {
                addTitleAndContentContainsInBooleanBuilder(searchBuilder,s);
            }
        }

        JPAQuery<Post> postJPAQuery = jpaQueryFactory.selectFrom(post)
                .where(searchBuilder)
                .orderBy(post.createdTime.desc());

        long count = postJPAQuery.stream().count();
        List<Post> results = getQuerydsl().applyPagination(pageable, postJPAQuery).fetch();

        return new PageImpl<>(results, pageable,count);
    }


}
