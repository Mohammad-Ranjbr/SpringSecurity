package com.eazybytes.eazyschool.config;

import com.eazybytes.eazyschool.handler.CustomAuthenticationFailureHandler;
import com.eazybytes.eazyschool.handler.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // Creating a custom login page: For this, a controller called LoginController is created that manages the /login path. This controller displays the login.html page.
        // But there was a problem: Spring Security by default uses the POST method to display the login page, while this controller used the GET method,
        // so the default Spring Security page was displayed.
        // Too Many Redirects Problem: After applying the changes, a new problem appeared: the "Too Many Redirects" error.
        // The reason for this problem was that Spring Security tried to redirect the user to the login page,
        // but because the login page itself required authentication, the user was constantly redirected to this page.
        // To fix this problem, the /login path and any paths starting with it (using permitAll) were exempted from authentication.

        // In Spring Security, these credentials are processed through the UsernamePasswordAuthenticationFilter filter.
        // By default, this filter extracts username and password parameters from the request and checks them.
        // Post-Login Redirection
        // One of the problems mentioned in this text is that after a successful login, the user is redirected to the page they have previously viewed or to the home page (e.g. /home).
        // This behavior can be annoying for users, especially if we want to direct users to a specific page, such as a dashboard.
        // The defaultSuccessUrl() method is used. This method allows us to set a specific path (such as /dashboard) as the default page after a successful login.
        // This will make the user always be directed to this page after successful login, without needing to refer to the previous page or the main page.
        // By default, Spring Security uses username and password parameters for authentication. But sometimes we need to change these parameters. For example,
        // use userID instead of username and secretPWD instead of password.
        // After making these changes in the configuration, we must also apply the necessary changes in the HTML form of the login page (for example, login.html).
        // That is, the names of the input fields for username and password must be matched with these new values
        // Login Failed: If the username or password is incorrect, Spring Security automatically returns the user to the login page and adds the ?error parameter to the URL.

        // defaultSuccessUrl: This method is used in Spring Security settings to determine the path to which the user will be redirected after
        // successful login (with correct username and password). For example, the user may be redirected to the dashboard after successful login.
        //failureUrl: This method specifies the path to be redirected to when the user fails to login (such as entering an incorrect username or password).
        // Here, the instructor explains that parameters such as error=true can be added to the URL.
        // Then in the controller, this parameter can be used to display an error message to the user.

        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/", "/home", "/holidays/**", "/contact", "/saveMsg",
                                "/courses", "/about", "/assets/**","/login/**").permitAll()) //   /login/** For when the user logout  ->  /login?logout
                .formLogin(flc -> flc.loginPage("/login").usernameParameter("userid").passwordParameter("secretPwd")
                        .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true")
                        .successHandler(customAuthenticationSuccessHandler).failureHandler(customAuthenticationFailureHandler))
                .logout(loc -> loc.logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).clearAuthentication(true)
                        .deleteCookies("JSESSIONID"))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}EazyBytes@12345").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$12$88.f6upbBvy0okEa7OfHFuorV29qeK.sVbB9VQ6J6dWM1bW6Qef8m")
                .authorities("admin").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * From Spring Security 6.3 version
     *
     * @return
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


}
