package com.example.SpringSecurity_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EasyBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyBankBackendApplication.class, args);
	}

	// Security is an non-functional requirement security is very important similar or scalability , performance and availability.
	// Security should be considered right from development phase itself along with business logic
	// To better understand Spring Security, you should first familiarize yourself with the concepts of Servlet and Filter.
	// These two concepts are used in the Java ecosystem to manage requests and responses.
	// A Servlet is an object that receives HTTP requests and converts them into Java code to perform the necessary processing.
	// Servlets run inside a web server or servlet container (Web Server) such as Tomcat or JBoss.
	// Filters are used to perform processing before the request reaches the servlets and after processing by the servlets.
	// These filters are specifically used in Spring Security to implement security logic.
	// Spring Security uses filters to manage security in web applications. These filters intercept requests and
	// check if the user is authenticated. If the user is not authenticated, the request will be redirected to the login page.
	// Most of the security logic in Spring Security is implemented through filters.
	// Web servers or Servlet Containers are responsible for converting HTTP requests into ServletRequests that contain
	// the data sent by the client. Then this request is sent to the corresponding servlet.
	// The request is sent from the client to the server, processed in the server, and then the response
	// is prepared as a ServletResponse and sent to the client.

	// 1. Security Filters
	// In Spring Security, there is a set of security filters called Security Filters that process
	// incoming requests to the application. These filters work in a chain and each one is responsible
	// for specific tasks such as authentication and authorization. Some of the most important filters are:
	// UsernamePasswordAuthenticationFilter: This filter is responsible for processing login forms.
	// It receives user input such as username and password and processes it for authentication.
	// BasicAuthenticationFilter: Used to support Basic Authentication, which uses basic information encoded in the HTTP request header.
	// BearerTokenAuthenticationFilter: Used for authentication using JWT tokens (JSON Web Tokens).
	// ExceptionTranslationFilter: Handles and processes security-related exceptions, such as authentication or authorization errors.
	// 2. Authentication Process
	// When a user tries to access a protected resource, his request is first sent to one of the authentication filters
	// (such as UsernamePasswordAuthenticationFilter). This filter parses the request and extracts
	// the user's authentication information (such as username and password).
	// 3. Convert information to authentication object (Authentication Object)
	// After receiving user information, the corresponding filter converts this information into an object called Authentication.
	// This object contains user information, such as username and password, as well as the user's authentication status (such as authenticated or not).
	// 4. Authentication Manager
	// AuthenticationManager is the intermediary responsible for performing the authentication process using the Authentication object.
	// This handler typically sends the authentication request to one or more AuthenticationProviders.
	// 5. Authentication Providers
	// AuthenticationProviders are the parts of Spring Security that handle the actual authentication process.
	// Each AuthenticationProvider is responsible for authenticating a specific type of user. For example,
	// DaoAuthenticationProvider is used to authenticate users using username and password in the database.
	// 5.1 Checking user information (UserDetailsService)
	// Each AuthenticationProvider uses a UserDetailsService to get user details. This service is typically connected
	// to a database and retrieves user information such as usernames, passwords, and user roles.
	// 5.2 Decoding and checking the password (Password Encoder)
	// For added security, users' passwords are usually stored encrypted in the database.
	// Password Encoder is responsible for decoding the password provided by the user and comparing it with the password stored in the database.
	// 6. Authentication Result
	// After performing all these steps, the AuthenticationManager returns the authentication result to the security filter:
	// Success: If the user information is valid and the password is correct, the user is considered authenticated.
	// In this case, the updated Authentication with the status "authenticated" is
	// stored in a SecurityContext and the user request continues further processing.
	// Failed: Throws an AuthenticationException if the user information is not valid or the password is incorrect.
	// ExceptionTranslationFilter catches this exception and redirects the user to a login page or an error page.

}
