package com.example.SpringSecurity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    // Opaque tokens:
    // These tokens are random strings that do not contain any meaningful information.
    // Whenever a client (e.g. an application or browser) sends a request to the server, this token must be sent along with the request.
    // The auth server is responsible for maintaining and managing these tokens. For validation,
    // another server (e.g. an API) must contact the authentication server to ensure that the token is valid.
    // This validation process depends on the connection with the central server. That is,
    // if your application needs to validate tokens, it must connect to the authentication server for every request.
    // The main use of these tokens is in secure internal networks, where the connection to the central server is reliable and fast.
    // Opaque tokens are usually used in scenarios where the system does not need to be independent from the authentication server and can remain connected to that server.
    // These tokens are especially suitable for internal systems or secure networks where token validation can be easily managed.
    // JWT tokens are suitable for distributed systems or microservices that require independence and functionality without frequent server contact.
    // In these scenarios, a JWT can simply be used to validate the token locally.
    // Advantages of using tokens:
    // 1. Reducing the display of user credentials on the network:
    // When we use tokens, the user only submits their credentials (username and password) once when logging in. On subsequent requests,
    // instead of sending the username and password, the token is sent as a substitute.
    // This means that the user's sensitive information is transmitted only once in the network and is less vulnerable to theft.
    // More security: because the user's main information is sent only once, the possibility of theft of this information is minimized.
    // 2. Setting the token expiration time:
    // One of the most important advantages of tokens is that they can be set to expire.
    // This means that even if a hacker gains access to the token, it will no longer be usable after some time.
    // 3. Self-Contained Tokens:
    // The instructor specifically mentions JWT (JSON Web Token) which contains internal information about the user.
    // This means that the JWT token itself contains data such as the user ID, roles, and permissions. For this reason:
    // No need to query the server: Whenever the client wants to check the user's information, it can directly use the data inside the JWT.
    // There is no need to send a request to the server to get this information.
    // Reduced pressure on the server: the server does not need to process or retrieve user information from the database every time; All the required information is stored in the token.
    // 4. Reusability and Single Sign-On (SSO):
    // One of the important features of tokens is that one token can be used in several services or applications.
    // This advantage leads to Single Sign-On (SSO), where a user can access multiple services with a single login.
    // 5. Cross-Platform Compatibility:
    // Tokens can be used in different systems due to their simple and standardized design.
    // Usability in all types of devices: Tokens can be easily used in web, mobile and even IoT (Internet of Things) devices.
    // 6. Statelessness:
    // One of the most important advantages of using tokens is the ability to be stateless.
    // Here it means that the tokens contain all the information needed for authentication, so that the server does not need to maintain user sessions.
    // Suitable for microservices: Stateless is a big advantage in microservices architecture where scalability and reliability are important.
    // Requests can be sent to any of the server instances, because all the information needed for authentication is available in the token, and there is no need to store and manage state.
    //  What is JWT (JSON Web Token)?
    // JWT is a type of token used for authentication and authorization. With the help of JWT,
    // instead of being stored in the session on the server, the user's information is placed in the token itself and sent to the server in every request.
    // This makes the servers do not need to manage the session, which is an important advantage in distributed systems.
    // JWT structure:
    // JWT consists of three parts:
    // Header:
    // The header contains information about the type of token and the algorithm used for the digital signature. Normally, this section contains two fields:
    // alg: Hash algorithm which is usually HS256 (HMAC + SHA-256).
    // typ: Token type, usually "JWT".
    // This data is encoded in Base64URL format.
    // Payload:
    // The payload section contains data about the user or claims. For example, information such as:
    // sub: user ID (subject).
    // name: User name.
    // iat: token issuance time (issued at time)
    // You can also store other desired data such as roles or authorities in this section.
    // It is important that sensitive data such as passwords are not stored in this section, as the data is only Base64URL encoded and can be decrypted.
    // Signature (digital signature):
    // This section is created using an algorithm such as HMAC-SHA256 and a secret key. The signature is generated as follows:
    // Base64 header and payload are connected by a dot.
    // The result of the header and payload combination along with the secret key are hashed through the SHA-256 algorithm.
    // The output of this hash is added to the JWT as a signature.
    // A digital signature ensures that the contents of the token have not been tampered with. If someone tries to change the payload,
    // the digital signature will no longer be valid and the server will not accept the token.
    // The digital signature on the JWT ensures that the token was issued by the server and has not been tampered with.
    // If someone wants to change the contents of the token, since the signature was generated using the server's secret key,
    // it is not possible to generate a new signature without the secret key.
    // The secret key must be well protected, because if it is leaked, anyone can generate fake tokens that look authentic.
    // To increase security, the secret key should not be stored in the program code, and it is better to use secure mechanisms such as Secrets Vault or Environment Variables to store it.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    }

    // If the request is to the /user path: the filter is executed. Because the shouldNotFilter value is false (should not ignore the filter).
    // If the request is to another path (such as /home, /api, ...): the filter is not executed, because the shouldNotFilter value is true (it should not be filtered).

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/user");
    }

}
