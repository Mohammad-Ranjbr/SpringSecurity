package com.example.SpringSecurity.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationEvents {

    // By default, Spring Security does not emit authorization verification events, as this can cause a large volume of events.
    // Although these events can be enabled with certain configurations, they are not recommended for most projects due to their large volume.

    @EventListener
    public void onFailure(AuthorizationDeniedEvent deniedEvent){
        log.error("Authorization failed for the user : {} due to : {}",deniedEvent.getAuthentication().get().getName(),
                deniedEvent.getAuthorizationDecision().toString());
    }

}
