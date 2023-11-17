package com.pain_care.pain_care.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
        private  UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional <User> user = userRepository.findByEmail(username);
         return  user.map(UserDetailsinfo::new)
           .orElseThrow(()-> new UsernameNotFoundException("UserName not found"+ username));
           
                
    }
}
