package com.example.SpringSecurity.filter;

import com.example.SpringSecurity.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    // When the user logs in and the login information is correct, the server sends a JWT token to the user's browser in its response.
    // This token contains the user's identity information, which is sent to the server in subsequent requests so that the user does not need to log in again.
    // In Angular, after a successful login, the token is received from the response header and stored in the session storage.
    // Session storage is used for the reason that the information stored in it is deleted when the browser tab or the browser itself is closed.
    // Session Storage is more secure than Local Storage because the data is kept only as long as the browser tab is open.
    // Interceptor in Angular is a type of service that is connected to every HTTP request sent from the browser
    // to the server and has the ability to manipulate the request. We use the Interceptor to add the JWT token to the header of each request.
    // When the user logs out, the JWT token must be deleted from the session storage to prevent any misuse.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException {

        String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);
        if(jwt != null){
            Environment env = getEnvironment();
            if(env != null){
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                if(secretKey != null){
                    Claims claims = Jwts.parser().verifyWith(secretKey)
                            .build().parseSignedClaims(jwt).getPayload();
                    String username =String.valueOf( claims.get("username"));
                    String authorities = String.valueOf(claims.get("authorities")); // authorities : as string separate with comma
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/user");
    }

}
