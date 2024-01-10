package com.pain_care.pain_care.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.domain.UserDetailsinfo;
import com.pain_care.pain_care.repos.UserRepository;

@Component
public class UserinfoService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);

        return userOptional.map(user -> {

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            return new UserDetailsinfo(user, authorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
