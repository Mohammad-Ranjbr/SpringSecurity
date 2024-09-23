package com.example.SpringSecurity.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEvents {

    // Authentication Success Event: This event is issued when the user is successfully logged in. In Spring Security,
    // the ProviderManager class is responsible for emitting this event. If the authentication is successful,
    // Spring Security publishes this event and along with it, an Authentication object (containing user information) is sent.
    // How to publish events:
    // If successful, the ProviderManager calls the publishAuthenticationSuccess method, which publishes the authentication success event.
    // In case of authentication failure, the publishAuthenticationFailure method is called, which publishes the authentication failure event.
    // Handling various authentication failures: The AbstractAuthenticationFailureEvent class is used to handle various authentication failure events
    // such as "incorrect information" or "expired credentials". Based on the type of failure, an appropriate object is created and the corresponding event is emitted.
    // Event listener implementation:
    // Developers must define a class that listens for these events:
    // For authentication success, a method is defined with the AuthenticationSuccessEvent parameter.
    // For authentication failure, a method is defined with the AbstractAuthenticationFailureEvent parameter.
    // To simplify the Logger creation process, the @Slf4j annotation is used, which automatically provides a Logger for the class.
    // You must use @EventListener for these methods to receive events. This annotation belongs to Spring Boot and allows methods
    // to automatically listen for authentication events (to listen for AuthenticationSuccessEvent and AuthenticationFailureEvent).

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent successEvent){
        log.info("Login successful for the user : {}",successEvent.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failureEvent){
        log.error("Login failed for the user : {} due to : {}",failureEvent.getAuthentication().getName(),
                failureEvent.getException().getMessage());
    }

}
