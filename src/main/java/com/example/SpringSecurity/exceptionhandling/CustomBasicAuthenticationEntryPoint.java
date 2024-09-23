package com.example.SpringSecurity.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Exception Translation Filter:
    // Spring Security uses the exception translation filter (ExceptionTranslationFilter) to manage exceptions.
    // This filter distinguishes between authentication exceptions and inaccessibility exceptions and forwards them to the appropriate handlers.
    // If it is an authentication exception, it is sent to an authentication entry point (AuthenticationEntryPoint).
    // If the exception is denied, it is sent to the AccessDeniedHandler.
    // Authentication Entry Point:
    // The AuthenticationEntryPoint interface is responsible for handling authentication exceptions. There are various implementations of this interface,
    // such as BasicAuthenticationEntryPoint for basic authentication. When a 401 Unauthorized response is sent, the WWW-Authenticate header is included with the realm details.
    // Access Denied Handler:
    // The AccessDeniedHandler interface is responsible for handling access-denied exceptions. By default,
    // this interface sends a 403 Forbidden response when a user tries to access a resource that they are not allowed to access.
    // Customization:
    // To customize responses to 401 or 403 exceptions (such as changing the body or headers), you can create new implementation
    // classes of AuthenticationEntryPoint and AccessDeniedHandler and override their methods to define your custom behavior.

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // Populate dynamic value
        LocalDateTime currentTimeStamp = LocalDateTime.now();
        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
        String path = request.getRequestURI();
        response.setHeader("eazybank-error-reason", "Authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        // Construct the JSON response
        String jsonResponse = String
                .format("{\"+timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}"
                , currentTimeStamp, HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase()
                , message, path);
        response.getWriter().write(jsonResponse);
    }

}
