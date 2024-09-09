package com.example.SpringSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EasyBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyBankBackendApplication.class, args);
	}

	// Security is an non-functional requirement security is very important similar scalability , performance and availability.
	// Security should be considered right from development phase itself along with business logic
	// To better understand Spring Security, you should first familiarize yourself with the concepts of Servlet and Filter.
	// These two concepts are used in the Java ecosystem to manage requests and responses.
	// A Servlet is an object that receives HTTP requests and converts them into Java code to perform the necessary processing.
	// Servlets run inside a web server or servlet container such as Tomcat or JBoss.
	// Filters are used to perform processing before the request reaches the servlets and after processing by the servlets.
	// These filters are specifically used in Spring Security to implement security logic.
	// Spring Security uses filters to manage security in web applications. These filters intercept requests and
	// check if the user is authenticated. If the user is not authenticated, the request will be redirected to the login page.
	// Most of the security logic in Spring Security is implemented through filters.
	// Web servers or Servlet Containers are responsible for converting HTTP requests into ServletRequests that contain
	// the data sent by the client. Then this request is sent to the corresponding servlet.
	// The request is sent from the client to the server, processed in the server, and then the response
	// is prepared as a ServletResponse and sent to the client.

	// 1. (Login Request)
	// The user submits login information (username and password) through the login form. This request is sent to the controller,
	// but in Spring Security it is handled by a chain of security filters by default.
	// 2. (Security Filter Chain)
	// The sent request passes through a chain of security filters. One of the most important filters for
	// authentication is the UsernamePasswordAuthenticationFilter, which is derived from the AbstractAuthenticationProcessingFilter class.
	// 3. AbstractAuthenticationProcessingFilter:
	// AbstractAuthenticationProcessingFilter is an abstract class used for authentication filters. The actual
	// authentication logic is implemented in subclasses such as UsernamePasswordAuthenticationFilter.
	// 4. UsernamePasswordAuthenticationFilter:
	// UsernamePasswordAuthenticationFilter is responsible for extracting username and password from HttpServletRequest.
	// This information is then packaged into a UsernamePasswordAuthenticationToken object
	// (This object represents an authentication request and uses the Authentication implementation.
	// Then the authenticate() method of ProviderManager is called.) and sent to the AuthenticationManager.
	// 5. AuthenticationManager:
	// AuthenticationManager is the main interface for managing the authentication process.
	// The usual implementation of this interface in Spring Security is ProviderManager.
	// 6. ProviderManager
	// The ProviderManager is responsible for searching between multiple AuthenticationProviders to validate credentials.
	// Each AuthenticationProvider may perform a specific type of authentication. For database-based authentication, DaoAuthenticationProvider is usually used.
	// This object represents an authentication request and uses the authentication implementation. Then the authenticate() method of ProviderManager is called.
	// 7. AuthenticationProvider
	// One of the AuthenticationProviders, such as DaoAuthenticationProvider, handles the authentication process.
	// In this step, the username and password sent by the user are compared with the data stored in the database.
	// 8. UserDetailsService
	// DaoAuthenticationProvider depends on UserDetailsService to retrieve user information.
	// This interface loads user information from a database or any other data source.
	// This object represents an authentication request and uses the authentication implementation. Then the authenticate() method of ProviderManager is called.
	// 9. UserDetails
	// A class that uses the UserDetails interface is responsible for providing user details such as username, password, and roles.
	// 10. PasswordEncoder:
	// PasswordEncoder is used to compare the password entered by the user with the password stored in the database. This class encrypts and validates the password.
	// 11. Authentication Object
	// If the credentials are valid, an Authentication object (such as UsernamePasswordAuthenticationToken)
	// is created and stored in the SecurityContext. Otherwise, an exception (such as BadCredentialsException) is thrown and the user is redirected to the login page.
	// 12. SecurityContextHolder
	// After successful authentication, the Authentication object is stored in the SecurityContextHolder.
	// This object notifies the system that the user has been successfully authenticated and is accessible throughout the application via the SecurityContextHolder.

}
