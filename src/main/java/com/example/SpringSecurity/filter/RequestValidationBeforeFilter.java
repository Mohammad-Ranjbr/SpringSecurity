package com.example.SpringSecurity.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Requests are specifically of HTTP type. For this reason, you need to typecast the generic ServletRequest object to a more specific
        // HttpServletRequest object so that you can access the specific methods and properties of the HTTP protocol.
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String header = req.getHeader(HttpHeaders.AUTHORIZATION); // Username and Password inside authorization header
        if(header != null){
            header = header.trim();
            if(StringUtils.startsWithIgnoreCase(header, "Basic ")){ // Authorization Header : Base64(Basic username:password)
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8); // Base64(username:password)
                byte[] decoded;
                try{
                    decoded = Base64.getDecoder().decode(base64Token);
                    String token = new String(decoded, StandardCharsets.UTF_8);
                    int delim = token.indexOf(":");
                    if(delim == -1){
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }
                    String email = token.substring(0,delim);
                    if(email.toLowerCase().contains("test")){
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                } catch (IllegalArgumentException exception){
                    throw new BadCredentialsException("Invalid basic authentication token");
                }
            }
        }
        chain.doFilter(request,response); // add custom filter inside filter chain
    }

}
