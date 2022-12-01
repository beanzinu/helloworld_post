package com.helloworldweb.helloworld_post.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
//@Table(indexes = {@Index(name = "email_index",columnList = "email")})
public class User implements UserDetails {

    @Id
    private Long id;

    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<PostSubComment> subComments = new ArrayList<>();

    private String email;
    private String profileUrl;
    private String nickName;

    @Transient
    private Collection<SimpleGrantedAuthority> authorities;

    @Builder
    public User(Long id, String email, String profileUrl, String nickName,List<PostSubComment> postSubComments) {
        this.id = id;
        this.email = email;
        this.posts = new ArrayList<>();
        this.profileUrl = profileUrl;
        this.nickName = nickName;
        this.subComments = postSubComments==null ? new ArrayList<>() : postSubComments;
    }

    public void updateUser(Map<String,String> map)
    {
        this.id = Long.valueOf(map.get("id"));
        this.email = map.get("email");
        this.profileUrl = map.get("profileUrl");
        this.nickName = map.get("nickName");
    }

    public void addPost(Post post){
        this.posts.add(post);
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return null;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
