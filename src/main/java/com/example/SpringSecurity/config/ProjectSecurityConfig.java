package com.example.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    // The problem is that the Spring Security framework protects all APIs by default.
    // But the goal is to customize Spring Security's default behavior with specific business needs.
    // First we need to understand which part of the code in Spring Security is responsible
    // for this default behavior. The class in question is SpringBootWebSecurityConfiguration.
    // This class and its defaultSecurityFilterChain method secure all HTTP requests by default and protect APIs for public access.

    // http.httpBasic(withDefaults()): This line enables Basic HTTP authentication. In this method,
    // the user sends their username and password as Base64 encoded in each request header.

    // SecurityFilterChain is an interface in Spring Security that is used to define and apply security filters.
    // These filters are used to process and security HTTP requests. In general,
    // these classes define what filters should be applied to requests and the order in which they are applied.
    // Whenever an HTTP request comes into the application, Spring Security decides how to process the request
    // through one or more SecurityFilterChains. Filters in this chain can perform various
    // tasks including authentication, access authorization, prevention of CSRF attacks and session management.

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

}
