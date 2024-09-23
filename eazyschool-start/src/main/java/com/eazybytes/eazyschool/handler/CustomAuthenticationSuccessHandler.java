package com.eazybytes.eazyschool.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // Implement custom SuccessHandler and FailureHandler
    // For more control over the login process, the instructor introduces another method using custom Handlers. This method allows the developer to apply
    // specific logic when the login succeeds or fails, in addition to redirecting the user to different pages. For example, sending emails,
    // registering information in the database or displaying special messages.
    // Custom Success Handler:
    // This class is implemented to control the operation if the login is successful. For example, we can record a message
    // in the logs when the user successfully logs in or redirect the user to a specific page
    // Custom Failure Handler:
    // This class is implemented to control the operation in case of login failure. For example, we can log a message or redirect the user to a specific page

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("Login successful for the user : {}",authentication.getName());
        response.sendRedirect("/dashboard");
    }

}
