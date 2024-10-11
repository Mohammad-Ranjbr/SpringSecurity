package com.example.SpringSecurity.config;

import com.example.SpringSecurity.exceptionhandling.CustomAccessDeniedHandler;
import com.example.SpringSecurity.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.SpringSecurity.filter.AuthoritiesLoggingAfterFilter;
import com.example.SpringSecurity.filter.AuthoritiesLoggingAtFilter;
import com.example.SpringSecurity.filter.CsrfTokenFilter;
import com.example.SpringSecurity.filter.RequestValidationBeforeFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

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

    // The default behavior is that there is no limit to the number of user sessions. That is, the user can log in without any restrictions from different devices or browsers.
    // When a new session is created and the maximum number of sessions allowed has been reached, Spring Security provides two behaviors:
    // Cancellation of old session: If the user creates a new session, his previous session will be canceled.
    // Prevent new sessions from being created: You can prevent new sessions from being created using maxSessionsPreventsLogin(true). This will keep the first session active and block subsequent login attempts.
    // Redirecting users to a custom page when their session expires or if they try to exceed the session limit is done using the expiredUrl() method.

    // Session Hijacking: It is an attack in which an attacker steals the user's Session ID to act on his behalf in the application.
    // How does it happen?
    // Session ID in URL: Some websites store Session ID in URL. An attacker can obtain this Session ID by accessing browser history or monitoring network traffic.
    // Session ID in cookies: Other organizations store the Session ID in cookies so that if an attacker can steal the cookie, they can use the Session ID to perform operations.
    // Network traffic: If the application uses HTTP instead of HTTPS, the attacker can obtain the Session ID by intercepting the network traffic.
    // Prevention methods:
    // Using HTTPS: Using HTTPS, Session IDs are transmitted encrypted and the attacker cannot see the traffic.
    // Short session timeout (Session Timeout): Reducing the active time of the Session ID, especially in public places such as libraries, can mean that even if an attacker obtains the Session ID, he does not have much time to exploit it.
    // Asking the user about using a public computer: Some programs ask the user during login if he is using a public system, and if the answer is positive, they take actions such as not saving the Session ID in cookies.

    // Session Fixation
    // Definition: In this attack, the attacker uses a specific Session ID for the victim after the victim logs in to the system.
    // When an attacker enters a website, he receives a valid Session ID from the server.
    // The attacker sends a deceptive link (containing his Session ID) to the victim, for example via email offering a discount or reward.
    // The victim clicks on the link and enters the site and logs in with the attacker's Session ID.
    // Once the victim logs in, all user information (such as payment details) is linked to the attacker's Session ID.
    // Using their Session ID (which is now linked to the victim's information), the attacker can gain access to the victim's account and exploit it.
    // Prevention methods:
    // Spring Security prevents Session Fixation attacks by default. This prevention is done by changing the Session ID after user authentication, so that if the attacker has created an initial Session ID, after the victim login, a new Session ID is created that the attacker does not know. There are three strategies to manage this issue:
    // Change Session ID: Session ID changes after login, but session information is preserved. This strategy is used by default by Spring Security.
    // New Session: A new session is created with a new Session ID and the information of the previous session is not copied (except for security related information).
    // Migrate Session: A new session is created with a new Session ID and all the information of the previous session is transferred to the new session.
    // http.sessionManagement(sessionManagement -> sessionManagement.sessionFixation(sessionFixation -> sessionFixation.newSession()));

    // CORS (Cross-Origin Resource Sharing)
    // A security mechanism that web browsers use to control which domains can access resources on a server. This mechanism is used to prevent insecure requests from other domains.
    // CORS problem scenario:
    // Let's say you have a web application running on the example.com domain. This application sends a request to receive information from an API server located in another domain, for example,
    // api.anotherdomain.com. Due to browser security policies, this request may be rejected unless the API server has explicitly allowed access to the example.com domain.
    // How CORS works:
    // CORS works by using a series of HTTP headers that the server adds to its response. These headers specify whether a domain is allowed to send requests to the server or not.
    // Access-Control-Allow-Origin Header: This header specifies which domains can access the server's resources.
    // CSRF (Cross-Site Request Forgery)
    // A type of security attack in which an attacker tries to abuse the credentials of an authenticated user and send invalid requests to the server on his behalf.
    // This attack usually occurs when a user authenticates to a website while keeping their browser open.
    // CSRF problem scenario:
    // Suppose a user logs into their banking website and then simultaneously goes to a malicious website. Through the malicious website,
    // the attacker tries to send requests such as money transfer to the bank server. Because the user is logged into their account and the session cookies are valid,
    // the server accepts the request, even if the user does not actually intend to perform this operation.
    // How CSRF works:
    // The user logs into their account on a trusted site (e.g. bank.com).
    // At the same time, the user enters a malicious site (e.g. evil.com).
    // The malicious site sends a POST request to the bank site without the user's knowledge, through invisible forms or requests.
    // Since the user is logged into their bank account and there are authentication cookies, the bank server confirms the request and the unwanted operation is performed.
    // Preventing CSRF:
    // To prevent CSRF, a security token (CSRF Token) is usually used. This token is sent to every form or request and must correctly and validly match every request sent to the server.
    // The server also checks the validity of this token and rejects the request if it is not valid.
    //  Difference between CORS and CSRF:
    // CORS: Used to control and restrict requests from external domains to the server. Its main purpose is to ensure that server resources are only accessible by authorized domains.
    // CSRF: An attack where existing user credentials are used to send fake requests. This attack usually takes place via a malicious form on an untrusted website.
    // CORS is more concerned with controlling the access level of domains, while CSRF is concerned with preventing fraudulent requests by abusing a user's authentication session.

    // CSRF (Cross-Site Request Forgery) is a type of attack in which an unauthorized user can perform an operation in a web application using the credentials of another user.
    // For example, a user may log into a banking website and then visit a malicious website that sends a hidden request to the banking website to transfer money.
    // How it works: These attacks are usually carried out through the use of user cookies that are automatically sent in requests.
    // A malicious website can send a request to a banking website (or any other website) that causes an unauthorized action to be performed.
    // Spring Security's default configuration includes CSRF protection, and this can be incorrectly disabled.
    // When CSRF protection is completely disabled, POST requests can be sent without any restrictions, which is not recommended for production applications.
    // When the CSRF configuration is enabled, Spring Security blocks POST requests and issues a 403 (Forbidden) error.
    // A 403 error means that the user is trying to submit data without a valid CSRF token.
    // To avoid these errors, there are two options:
    // Disable CSRF completely: This is not recommended as it compromises the security of the application.
    // Understanding and Implementing CSRF: This option involves understanding how a CSRF attack works and then implementing a CSRF token in the application to verify that requests are valid.
    // CSRF Token: One of the important ways to protect against CSRF is to use CSRF tokens. These tokens are added to every POST, PUT or DELETE request to prevent CSRF attacks.
    // CSRF can lead to the disclosure of sensitive information or the modification of important data in web applications.
    // Because of user credentials (such as cookies) that are automatically sent to websites, hackers can perform unauthorized actions without logging into the user's account.
    // How CSRF Token works
    // Token generation: Every time a user loads a form in the web application, a unique CSRF token is generated and placed in the form.
    // Submit Token: When the user submits the form, this token is sent to the server along with the form data.
    // Token Validation: The server verifies this token. If the token in the request does not match the token generated when the form is loaded, the request is rejected.
    // Even if CORS (Cross-Origin Resource Sharing) policies are implemented, CSRF can still occur.
    // This is because requests are sent from within the browser to the same domain, and the browser has no reason to reject them.

    // Step 1: Sign in to Netflix
    // The user logs in to the Netflix website, and after logging in, a cookie (for example, abc123) is stored in the browser to identify the user.
    // This cookie is only associated with the netflix.com domain and is not sent in requests to other domains.
    // Step 2: Open a malicious website
    // After watching a video, the user opens a new tab in their browser and goes to a malicious website (eg, evil.com).
    // This malicious website contains links or banners that entice the user to click.
    // Step 3: Execute the CSRF request
    // When the user clicks on the link, the hacker has created an embedded form on the malicious website that sends a request to netflix.com.
    // This form may contain parameters that identify the hacker's goal, such as changing the user's email address to the hacker's email address.
    // When the user clicks on the link, the form is submitted automatically and the browser adds the abc123 cookie to the request.

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
         http .securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() { // Anonymous Class
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
                        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }
                }))
                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // Only HTTPS
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/contact","/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                 .addFilterAfter(new CsrfTokenFilter(), BasicAuthenticationFilter.class)
                 .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                 .addFilterAfter(new AuthoritiesLoggingAfterFilter(),BasicAuthenticationFilter.class)
                 .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
//                                .requestMatchers("myAccount").hasAuthority("VIEWACCOUNT")
//                .requestMatchers("myBalance").hasAnyAuthority("VIEWBALANCE","VIEWACCOUNT")
//                .requestMatchers("myLoans").hasAuthority("VIEWLOANS")
//                .requestMatchers("myCards").hasAuthority("VIEWCARDS")
                .requestMatchers("myAccount").hasRole("USER")
                .requestMatchers("myBalance").hasAnyRole("USER","ADMIN")
                .requestMatchers("myLoans").hasRole("USER")
                .requestMatchers("myCards").hasRole("USER")
                .requestMatchers("/user").authenticated()
                .requestMatchers("notices","contact","/error","/register","/invalidSession").permitAll());
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
