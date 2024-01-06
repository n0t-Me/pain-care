package com.pain_care.pain_care.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.pain_care.pain_care.service.UserinfoService;

//import com.pain_care.pain_care.domain.User;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import com.pain_care.pain_care.service.UserService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userdetailservice() {
        //UserDetails user = User.withUsername(null)
        //  .password( encoder.encode("password"))
        // .build();
        //return new InMemoryUserDetailsManager(user);
        return new UserinfoService();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/", "/assets/**", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/users/register").permitAll()
                                .anyRequest().authenticated()

                ).formLogin(formLogin ->
                        formLogin
                                .loginPage("/users/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/") // Redirect after successful login
                                .permitAll()

                ).logout(logout ->
                        logout
                                .logoutUrl("/logout") // Customize the logout URL if needed
                                .logoutSuccessUrl("/") // Redirect after successful logout
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userdetailservice());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
