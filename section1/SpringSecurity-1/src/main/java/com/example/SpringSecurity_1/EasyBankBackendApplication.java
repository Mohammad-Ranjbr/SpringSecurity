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
	// To better understand Spring Security, you should first familiarize yourself with the concepts of Servlet and Filter. These two concepts are used in the Java ecosystem to manage requests and responses.
	// A Servlet is an object that receives HTTP requests and converts them into Java code to perform the necessary processing. Servlets run inside a web server or servlet container (Web Server) such as Tomcat or JBoss.
	// Filters are used to perform processing before the request reaches the servlets and after processing by the servlets. These filters are specifically used in Spring Security to implement security logic.
	// Spring Security uses filters to manage security in web applications. These filters intercept requests and check if the user is authenticated. If the user is not authenticated, the request will be redirected to the login page.
	// Most of the security logic in Spring Security is implemented through filters.
	// Web servers or Servlet Containers are responsible for converting HTTP requests into ServletRequests that contain the data sent by the client. Then this request is sent to the corresponding servlet.
	// The request is sent from the client to the server, processed in the server, and then the response  is prepared as a ServletResponse and sent to the client.

}
