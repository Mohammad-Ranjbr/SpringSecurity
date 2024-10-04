package com.example.SpringSecurity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfTokenFilter extends OncePerRequestFilter {

    // OncePerRequestFilter is a helper class in Spring that ensures your filter is executed only once per request.
    // This is useful when you have filters that should not be run multiple times for the same request (such as security filters).
    // In this case, your filter will fire only once for each HTTP request, even if the request is processed to different filters.

    // CsrfToken Interface
    // CsrfToken is an interface that describes what a CSRF token should look like.
    // This interface contains methods that provide basic information about the CSRF token, such as header name, parameter name, and the token itself.
    // String getHeaderName()
    // This method returns the name of the HTTP header used to send the CSRF token.
    // String getParameterName()
    // This method returns the name of the URL parameter used to send the CSRF token to the server.
    // String getToken()
    // This method returns the actual CSRF token value. This token is used as a unique identifier to prevent CSRF attacks.
    // CsrfTokenRepository Interface
    // CsrfTokenRepository is an interface used to manage the storage and retrieval of CSRF tokens.
    // This interface is responsible for generating, storing and retrieving CSRF tokens and is generally used by the CSRF filter in Spring Security.
    // HttpSessionCsrfTokenRepository
    // This implementation stores the CSRF token in the session. However, storing the token in the session is generally not recommended, as it may cause security issues.
    // CookieCsrfTokenRepository
    // This implementation stores the CSRF token in the cookie. This method is preferable to saving in session because it provides better security.

    // Generate CSRF Token: In Spring Security, CSRF token is generated lazily; That is,
    // until a specific request (usually data change requests such as POST or PUT) reaches the server,
    // this token will not be generated. This filter is specifically defined for this purpose so that the
    // CSRF token is forcibly generated during the first request and the corresponding cookie is sent to the client.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Gets the token from the HttpServletRequest, but if the token hasn't been generated yet (due to "lazy loading"), this line may not have the token yet and return null.
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        // Render th token value to a cookie by causing the deferred token to be loaded
        // This line actually retrieves the CSRF token from the CsrfToken object.
        // If the token has not already been generated, this line will generate the token.
        // This way, when the getToken() method is called, Spring Security generates the token and adds it to the HttpServletRequest.
        // As a result, this line generates and stores the token in the request.
        csrfToken.getToken();
        filterChain.doFilter(request,response);
    }

}
