package com.example.SpringSecurity.config;

import com.example.SpringSecurity.exceptionhandling.CustomAccessDeniedHandler;
import com.example.SpringSecurity.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("prod")
@Configuration
public class ProjectSecurityProdConfig {

    // HTTPS ensures the security of data sent between the user and the server. By default, Spring Security accepts both HTTP and HTTPS requests.
    // In order for the application to accept only HTTPS requests in the production environment, settings must be made. In the prod profile,
    // sending an HTTP request causes an error because the request is redirected to the HTTPS port (usually 8443).

    // To configure this custom logic in Spring Security, you must use the exceptionHandling method and register a CustomAccessDeniedHandler object as a handler for access denied (403) errors.
    // This configuration is done globally and is not related to specific levels such as HTTP Basic or Form Login. That is, wherever a 403 error occurs in the application, this custom handler is executed.
    // When using AccessDeniedHandler or accessDeniedPage
    // If your application is a REST API and uses external clients such as mobile applications or Angular/React, it is better to use AccessDeniedHandler and JSON responses.
    // If your application is based on Spring MVC and includes a built-in user interface (UI), it is more appropriate to use accessDeniedPage and redirect the user to a specific error page.

    // By default, when a user logs in, a session is created by Spring Boot and Spring Security. This session has a default expiration time of 30 minutes.
    // When the user remains inactive for 30 minutes, his session will be automatically canceled and if the user performs an activity again, he will be returned to the login page.
    // If the session expiration time needs to be shorter or longer than the default time, this value can be configured in the application.properties file.
    // This value can be set using the server.servlet.session.timeout property. For example, you can use the command server.servlet.session.timeout=20m to set the expiration time of 20 minutes.
    // This property follows one rule: the minimum time allowed for a session to expire must be two minutes (120 seconds) or longer. This is because session expiration in less than two minutes is not reasonable for most applications.
    // When the user's session expires (for example, after 20 minutes of inactivity), the user is redirected to the login page by default, and the user may not understand the reason for this return.
    // To improve the user experience, the user may be redirected to another page (for example, a page with the message "Your session has ended, please log in again"). For this, the invalidSessionUrl property is used in the Spring Security configuration.
    // In addition to session expiration, there are scenarios where sessions may become invalid for some reason. For example, if the user changes the JSESSIONID value in the cookie and then tries to access the API, the session is recognized as an invalid session.
    // In these scenarios too, instead of redirecting the user to the login page, the user can be redirected to the page defined for invalidSessionUrl.

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession"))
                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // Only HTTPS
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("myAccount","myBalance","myLoans","myCards").authenticated()
                .requestMatchers("notices","contact","/error","/register").permitAll());
        // It is deprecated and cannot be disabled with the disable method, we must disable its entry
        // http.formLogin(flc -> flc.disable());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * From Spring  Security 6.3 version
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
