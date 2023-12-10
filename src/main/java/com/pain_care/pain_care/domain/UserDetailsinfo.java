package com.pain_care.pain_care.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsinfo implements UserDetails{

    private String email;
    private String password ;
    private String name;
    private Integer id;
    private String pic;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsinfo(User user, Collection<? extends GrantedAuthority> authorities) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.id = user.getId();
        this.pic = user.getPic();
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
         return true;  
    }
 
    public String getName() {
        return name;
    } 

    public Integer getId() {
        return id;
    } 

    public String getPic() {
        return pic;
    }
}
