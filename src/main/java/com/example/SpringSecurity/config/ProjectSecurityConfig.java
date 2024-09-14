package com.example.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    // SecurityFilterChain is an interface in Spring Security that specifies which security filters should be applied to HTTP requests and in what order.
    // These filters sequentially process the incoming requests and decide whether the request is allowed or not. In short,
    // the SecurityFilterChain defines how to apply security filters to requests. Each time an HTTP request is sent to the server,
    // this chain of filters is continuously run on that request. These filters may include the following:
    // Authentication: checks whether the user has verified his identity or not.
    // Authorization: Checks whether the user is authorized to access certain resources or not.
    // Session Management (Session Management): control the status and lifetime of the user session.
    // CSRF (Cross-Site Request Forgery) Protection: Prevents fake request attacks.
    // SecurityFilterChain allows you to flexibly have multiple filter chains and configure each chain for different URLs or different request types.

    // HttpSecurity is one of the main classes in Spring Security that is used to configure the security of web applications.
    // This class allows you to set various resource access, authentication, permissions, and other security features for HTTP requests.

    // 1. permitAll() method: This method allows you to make a specific route or API accessible to all users without the need for authentication.
    // For example, if you want a contact or notifications page to be available without any restrictions, you would use permitAll() .
    // If you use permitAll() along with anyRequest(), it means that all incoming requests to your web application will be unprotected. This is a big mistake,
    // because the whole system opens without any restrictions.
    // This method is mostly used for routes that should be given access to everyone (such as login pages or general information).
    // 2. denyAll() method: This method works the opposite of permitAll(). That is, it blocks all requests to any given API or route.
    // Even if the user is authenticated, they will not have access and will get a 403 Forbidden error.
    // This method is rarely used in real projects, but it can be useful if you need to block access to an API for a certain period of time. For example,
    // you may want to block access to a certain service for two months.
    // 3. requestMatchers() and various access controls
    // For more precise configuration, requestMatchers() can be used instead of using anyRequest().
    // This method allows you to specify specific routes and define different security policies for each route.
    // 4. Error paths (/error): In case of runtime errors, Spring Security by default redirects the user to an error page (/error).
    // This path is also protected by default, the user will not be able to see the details of the error.

    // formLogin(): This method is used to authenticate users through web forms. Users usually enter their username and password and this information is sent to the server to verify their identity.
    // For applications that require login through a form (such as regular web applications), this method is suitable.
    // httpBasic(): This method is used to authenticate users through HTTP headers. Credentials (username and password) are sent directly in the HTTP request header.
    // This method is suitable for REST APIs where interactions are usually done via HTTP headers.
    // Credentials (username and password) are sent in the Authorization header as Basic (Format: Basic BAse64(username:password)). This header is encoded in Base64, and it is recommended to use the HTTPS protocol for data protection.
    // Disable formLogin(): In projects based on REST APIs, login forms are usually not needed, and it is more appropriate to use httpBasic() or other header-based authentication methods.
    // Using deprecated methods (like formLogin() in certain scenarios) can cause problems in the future, as these methods may be removed in future versions of Spring Security.

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("myAccount","myBalance","myLoans","myCards").authenticated()
                .requestMatchers("notices","contact","/error").permitAll());
        // It is deprecated and cannot be disabled with the disable method, we must disable its entry
        // http.formLogin(flc -> flc.disable());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withUsername("user").password("{noop}123456").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{noop}654321").authorities("admin").build();
        return new InMemoryUserDetailsManager(user,admin);
    }

}