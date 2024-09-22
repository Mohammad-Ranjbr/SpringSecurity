package com.example.SpringSecurity.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// This been is activated when the prod profile is active

@Component
@Profile("prod")
@RequiredArgsConstructor
public class EazyBankProdUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(passwordEncoder.matches(pwd,userDetails.getPassword())){
            // Fetch Age details and perform validation to check if age > 18
            return new UsernamePasswordAuthenticationToken(username,pwd,userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    // Currently, we use Dao Authentication Provider as a default Authentication Provider. This provider is responsible for loading user information from the database and uses UserDetailsService for this.
    // Why do we need a custom Authentication Provider?
    // In some projects and organizational programs, more complex security needs are raised. Or you may want to apply certain rules such as age restriction or geo-restriction to authenticate users.
    // Spring Security uses Provider Manager, which is tasked with checking what authentication type is requested and calling the appropriate Provider.
    // The Provider Manager can identify which Provider to invoke using the Authentication Object type (e.g. UsernamePasswordAuthenticationToken or OAuth2AuthenticationToken).
    // If you need additional rules, such as checking the user's age (over 18 years) or restricting access to a specific country, you can implement a custom Authentication Provider.
    // This custom authentication is ultimately returned to the Provider Manager and a response is provided to the end user.
    // In the authentication process, the user's request is first received by security filters (such as UsernamePasswordAuthenticationFilter).
    // Then, the Authentication Object (such as UsernamePasswordAuthenticationToken) is generated and sent to the Authentication Manager.
    // Authentication Manager is responsible for selecting the appropriate Authentication Provider to process this request.
    // How does Provider Manager choose the right Provider?
    // The Provider Manager decides which Authentication Provider to invoke based on the type of Authentication Object created from the authentication request.
    // For example, if the Authentication Object is of type UsernamePasswordAuthenticationToken, the Provider Manager goes to the DaoAuthenticationProvider that uses the UserDetailsService to load the user.
    // In Spring Security, to build a Custom Authentication Provider, you must implement your own class from the AuthenticationProvider interface. This interface contains two main methods:
    // authenticate(Authentication authentication) which is used to perform the authentication process.
    // supports(Class<?> authentication) which specifies what type of Authentication Object this Authentication Provider supports.
    // authenticate (Authentication authentication):
    // This method is responsible for checking whether the user is valid or not.
    // Here you can get the username and password from the Authentication object and compare it with the information stored in the database or other services.
    // You can also add more complex logic such as validating based on the user's age, country, or even using OAuth2 tokens.
    // The final result of this method can be one of these three states:
    // Successful authentication: An Authentication object is returned indicating that the user has been authenticated.
    // Authentication failed: An exception (such as BadCredentialsException) is thrown.
    // supports(Class<?> authentication):
    // This method determines whether the AuthenticationProvider is capable of processing a particular type of authentication.
    // In complex scenarios, there may be multiple AuthenticationProviders. supports helps Spring Security decide which provider should be used for a particular authentication request.

}
