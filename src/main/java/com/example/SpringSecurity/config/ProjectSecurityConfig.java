package com.example.SpringSecurity.config;

import com.example.SpringSecurity.exceptionhandling.CustomAccessDeniedHandler;
import com.example.SpringSecurity.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.SpringSecurity.filter.CsrfTokenFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {

    // The problem is that the Spring Security framework protects all APIs by default.
    // But the goal is to customize Spring Security's default behavior with specific business needs.
    // First we need to understand which part of the code in Spring Security is responsible
    // for this default behavior. The class in question is SpringBootWebSecurityConfiguration.
    // This class and its defaultSecurityFilterChain method secure all HTTP requests by default and protect APIs for public access.

    // http.httpBasic(withDefaults()): This line enables Basic HTTP authentication. In this method,
    // the user sends their username and password as Base64 encoded in each request header.

    // SecurityFilterChain is an interface in Spring Security that specifies which security filters should be applied to HTTP requests and in what order.
    // These filters sequentially process the incoming requests and decide whether the request is allowed or not. In short,
    // the SecurityFilterChain defines how to apply security filters to requests. Each time an HTTP request is sent to the server,
    // this chain of filters is continuously run on that request. These filters may include the following:
    // Authentication: checks whether the user has verified his identity or not.
    // Authorization: Checks whether the user is authorized to access certain resources or not.
    // Session Management (Session Management): control the status and lifetime of the user session.
    // CSRF (Cross-Site Request Forgery) Protection: Prevents fake request attacks.
    // SecurityFilterChain allows you to flexibly have multiple filter chains and configure each chain for different URLs or different request types.

    // HttpSecurity is one of the main classes in Spring Security that is used to configure the security of web applications.
    // This class allows you to set various resource access, authentication, permissions, and other security features for HTTP requests.

    // 1. permitAll() method: This method allows you to make a specific route or API accessible to all users without the need for authentication.
    // For example, if you want a contact or notifications page to be available without any restrictions, you would use permitAll() .
    // If you use permitAll() along with anyRequest(), it means that all incoming requests to your web application will be unprotected. This is a big mistake,
    // because the whole system opens without any restrictions.
    // This method is mostly used for routes that should be given access to everyone (such as login pages or general information).
    // 2. denyAll() method: This method works the opposite of permitAll(). That is, it blocks all requests to any given API or route.
    // Even if the user is authenticated, they will not have access and will get a 403 Forbidden error.
    // This method is rarely used in real projects, but it can be useful if you need to block access to an API for a certain period of time. For example,
    // you may want to block access to a certain service for two months.
    // 3. requestMatchers() and various access controls
    // For more precise configuration, requestMatchers() can be used instead of using anyRequest().
    // This method allows you to specify specific routes and define different security policies for each route.
    // 4. Error paths (/error): In case of runtime errors, Spring Security by default redirects the user to an error page (/error).
    // This path is also protected by default, the user will not be able to see the details of the error.

    // formLogin(): This method is used to authenticate users through web forms. Users usually enter their username and password and this information is sent to the server to verify their identity.
    // For applications that require login through a form (such as regular web applications), this method is suitable.
    // httpBasic(): This method is used to authenticate users through HTTP headers. Credentials (username and password) are sent directly in the HTTP request header.
    // This method is suitable for REST APIs where interactions are usually done via HTTP headers.
    // Credentials (username and password) are sent in the Authorization header as Basic (Format: Basic BAse64(username:password)). This header is encoded in Base64, and it is recommended to use the HTTPS protocol for data protection.
    // Disable formLogin(): In projects based on REST APIs, login forms are usually not needed, and it is more appropriate to use httpBasic() or other header-based authentication methods.
    // Using deprecated methods (like formLogin() in certain scenarios) can cause problems in the future, as these methods may be removed in future versions of Spring Security.

    // In Spring Security, CSRF is enabled by default and provides protection for APIs that modify data (such as POST, PUT, DELETE).

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        // http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() { // Anonymous Class
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                        corsConfiguration.setAllowCredentials(true); // This line determines whether identity information (such as cookies or authentication tokens) is allowed to be sent with CORS requests.
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setMaxAge(3600L); // This line determines when the browser can cache CORS settings.
                        // In this example, by setting the value to 3600L (which equals 3600 seconds or one hour),
                        // the browser can cache this setting for one hour. This means that for subsequent requests from the same origin,
                        // the browser will not need to send a preflight request until this time expires.
                        return corsConfiguration;
                    }
                }))
                //.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(3).maxSessionsPreventsLogin(true))
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
                // When a cookie is created, this value is set correctly. If it is set true, only the browser has access to this cookie and sends it in every request,
                // because JavaScript needs to read this value from the cookies and put it in the header or body of the request. The value must be false
                .csrf(csrfConfig -> csrfConfig.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                // This code adds the CSRF filter after the BasicAuthenticationFilter. The reason for this arrangement
                // is that authentication must be done first so that we can generate the CSRF token for subsequent requests.
                .addFilterAfter(new CsrfTokenFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("myAccount","myBalance","myLoans","myCards","/user").authenticated()
                .requestMatchers("notices","contact","/error","/register","/invalidSession").permitAll());
        // It is deprecated and cannot be disabled with the disable method, we must disable its entry
        // http.formLogin(flc -> flc.disable());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    // The UserDetailsService interface is used as a core interface for loading user information.
    // This interface is responsible for retrieving user details (Username, Password, Authorities) that are required for authentication.
    // UserDetails in Spring Security is an interface that represents the details of a user for authentication. (Provides core user information.)
    // In Spring Security, there is a predefined class called org.springframework.security.core.userdetails.User
    // that is the default implementation of the UserDetails interface. This class helps you easily introduce
    // user information to Spring Security without having to write a custom implementation of the UserDetails interface.

    // When we want to build the user, it goes to the User class and the build method here first tries to encode the password.
    // It goes to the InMemoryUserDetailsManager class in the constructor of this class, which wants to create all the created users,
    // and calls the createUser method of the InMemoryUserDetailsManager class, which in this method first checks if there is a
    // user with this username, if there is not, a HashMap called users updates and adds new users to it

    // UserDetailsService: This interface contains a method used to load user information from any type of storage system (internal memory or database) based on username.
    // This interface is used when the organization only needs user authentication and does not need user management (such as creating, deleting or changing).
    // UserDetailsManager: This interface inherits from UserDetailsService and adds methods like createUser, deleteUser and changePassword.
    // Common implementations include InMemoryUserDetailsManager and JdbcUserDetailsManager. This interface is used in scenarios where organizations need complete user management (CRUD).

    // UserDetails shows basic user information such as username, password and authorities.
    // Authentication includes methods such as getPrincipal(), isAuthenticated() and getCredentials().
    // This interface checks the authentication success or failure status and manages the credentials during the process.
    // UserDetails focuses on storing user information that is required for authentication.
    // Authentication manages the state and authentication process.
    // Keeping these two interfaces separate reduces unnecessary overhead in the authentication process, especially in cases where some user details are no longer relevant after authentication.
    // UsernamePasswordAuthenticationToken class: This class is one of the most common implementations of the Authentication interface used in username and password login flows.
    // This class holds the user ID (Principal), credentials, which usually include a password, and authorities or roles.
    // After successful authentication, the Spring Security framework calls the eraseCredentials method to clear the credentials
    // (especially the password) and prevent unnecessary disclosure of sensitive information.

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user = User.withUsername("user")
//                .password("{noop}eazybankuser@123456")
//                .authorities("read")
//                .build();
//        // Convert plain text to hash value , So that if someone gets access to the code, they will not get access to the password
//        UserDetails admin = User.withUsername("admin")
//                .password("{bcrypt}$2a$12$1UN2OGBUcp4Opsfn.RJN5.IRgB5uqPC9tDoG.F/j3itwEqXu2uiPS")
//                .authorities("admin")
//                .build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }

    // JdbcUserDetailsManager: Provides CRUD operations for both users and groups. JdbcUserDetailsManager extends JdbcDaoImpl and implements UserDetailsManager
    // JdbcDaoImpl: UserDetailsService implementation which retrieves the user details (username, password, enabled flag, and authorities) from a database using JDBC queries.
    // A default database schema is assumed, with two tables "users" (username , password , enabled) and "authorities" (username , authority). The Users table
    //
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    // PasswordEncoderFactories Used for creating PasswordEncoder instances (default: BCryptPasswordEncoder)
    // BCryptPasswordEncoder uses the bcrypt algorithm, which is a password hashing algorithm
    // Salting: This implementation automatically combines each password with a unique "salt".
    // This means that even if two users use the same password, their hashed values will be different.
    // Adjustable difficulty support: bcrypt uses a difficulty parameter (cost factor) that controls the number of calculations required to generate a hash.
    // The higher this parameter is, the more time it takes to generate the hash, and as a result, higher security is provided.

    // Hashing
    // Hashing is one of the most widely used methods for securely managing passwords. In this method, the original data (such as a password) is converted into
    // a hash value that is irreversible. Typically, hashes are stored as long strings of random characters where small changes to the input can produce completely different output.
    // Hash algorithms such as SHA-256 and bcrypt are used to secure passwords. bcrypt is more secure due to its more secure structure and the use of "salt" which
    // adds a random value to each password. This makes it so that if two users have the same password, their hashes will be different.
    // Encryption
    // Encryption is another data protection method that converts data into unreadable formats. Unlike hashing, encryption is reversible.
    // This means that having the private key or the decryption key, the encrypted data can be restored to its original state.
    // Encryption is done with two main models:
    // Symmetric encryption: In this method, a key is used for encryption and decryption.
    // Asymmetric encryption: In this method, two keys are used: a public key for encryption and a private key for decryption.
    // Encryption is usually used to protect sensitive data such as messages or files, but is not suitable for passwords, as there is a possibility of password recovery.
    // Suppose application 1 wants to get a file from application 2, application 1 creates the public key and private key and sends the public key to application 2,
    // application 2 encrypts (Cipher ) the data with the public key and sends it to application 1. Now, if someone accesses the data in the middle,
    // application 2 encrypts (Cipher Data) the data with the public key and sends it to application 1. Now, if someone accesses the data in the middle,
    // he cannot use it because he does not have the private key, so he reaches application 1 and decrypts it with the private key.
    // Encoding
    // Encoding is simply a process of converting data into a format that can be understood by certain systems or protocols. For example,
    // in e-mails or data transmission over the Internet, Encoding is used to convert data into text form.
    // Base64 is one of the most popular encoding methods, usually used to transfer binary data (such as images or videos) to text format.
    // The most important thing about encryption is that it doesn't provide any security because the encryption is reversible and anyone who has the encrypted data can convert it to the original data.

    // Disadvantages of hashing in password management
    // A) Determinability of Hashes: Sameness of Hashes for Similar Inputs: If two different users use a simple password like 12345, the generated hash value will be the same for both users (e.g. AEF).
    // b) High speed in generating hashes: Fast performance of hash functions: Hash functions such as SHA-256 are very fast and can hash a large number of passwords in a short period of time.
    // Brute Force Attack
    // Method: The hacker tries all possible combinations of characters as a password and compares the hash of each combination with the hash values in the database.
    // Example: If the password is 12345 and its hash is AEF, the hacker will gradually try combinations 00000, 00001, ..., 12345 to reach AEF.
    // Rainbow Tables Attack
    // Method: The hacker creates a table of hash values for a set of common passwords.
    // Application: After obtaining the stored hashes, the hacker can quickly find the master password by looking up the hash value in the table.
    // Advantage: This method is much faster than exhaustive search because it does not need to recalculate hashes.
    // Solutions to deal with the disadvantages of hashing
    // To deal with the disadvantages of hashing and increase the security of password management, the following methods can be used:
    // A) Use of Salt:
    // Definition: Salt is a random value added to each password before hashing.
    // Advantage: It makes the hashes of two users different even if they use the same password. This makes omnibus lookup attacks and rainbow tables more difficult.
    // b) Use of Pepper:
    // Definition: Pepper is a fixed, secret value that is added to all passwords and is usually kept at the application level.
    // Advantage: Adding Pepper in addition to Salt increases security because even if Salt and hashes are leaked, passwords cannot be recovered without Pepper.
    // c) Using more resistant hash algorithms:
    // Examples: bcrypt, Argon2, PBKDF2.
    // Advantage: These algorithms include mechanisms such as multiple iterations and salt addition that increase the time required to perform sweeping search attacks and improve security.
    // d) Application of entry restrictions:
    // Method: Limit the number of failed login attempts.
    // Advantage: This method can prevent extensive search attacks because the hacker cannot access a large number of password combinations.

    // Overcome Hashing drawbacks, Brute force and Dictionary table attacks
    // 1. Use of Salt
    // Salt is a random value added to the password before hashing.
    // Each time a user registers a new password, a random value (eg "AEX2FDAC") is added to the original password (eg "12345").
    // For example, a password combination with Salt would be something like "AEX2FDAC12345".
    // This new combination is fed into the hash function and the final result, the hash, is stored.
    // Why is Salt important? If two users have the same password like "12345", they will have different hashes due to different salt.
    // Therefore, if an attacker can even get hold of the database hashes, he cannot use rainbow tables or ready-made
    // dictionaries to crack the password because the Salt value is unique for each user.
    // How it works at login: When a user enters their password, the system takes the stored Salt value for that user from the database,
    // adds it to the entered password, and calculates the new hash. If this new hash matches the stored hash, the login is successful.
    // 2. Using slow hashing algorithms
    // Some hashing algorithms such as Bcrypt, Scrypt, and Argon2 are specifically designed to deliberately slow down the hashing process.
    // Why is it important to slow down hashing? In exhaustive search attacks, the attacker tries a very large number of
    // possible password combinations to arrive at the correct password. If the hashing process is fast,
    // he can try millions of passwords in a short amount of time. But if hashing is slow (e.g. each hash takes 1-2 seconds),
    // the time required for each attack attempt increases dramatically.
    // Slow algorithms also use a lot of resources such as CPU and RAM. This makes attacking a hacker require a lot of computing resources and cost.
    // For example, guessing an 8-character password with a combination of uppercase and lowercase letters,
    // numbers, and special characters ، locking the account after several unsuccessful attempts can take years and cost an attacker a lot.

    // Login process using passwords:
    // When a user enters their username and password, Spring Security first retrieves the hashed password value for that username from the database.
    // Then, the salt stored along with the hash is extracted and the hashing process is performed again along with the entered password.
    // Finally, this new hash is compared with the existing hash in the database. If these two values are equal, the user is logged in; Otherwise, the login will fail.

    // PasswordEncoder has three methods:
    // encode(): used to hash the password during registration. This method takes the raw password, hashes it, and randomly generates and uses the salt.
    // matches(): Used to compare the password entered during login with the hash stored in the database. This method checks if the raw password matches the stored hash.
    // upgradeEncoding(): This method returns false by default.
    // If hashing is needed again (to increase security in very sensitive applications), this method should be overridden to return true and perform two-step hashing.

    // Although algorithms such as Scrypt and Argon2 are stronger and more advanced, Spring Security recommends using Bcrypt for the following reasons:
    //Ease of use: Bcrypt requires less configuration than Scrypt and Argon2, making it more suitable for everyday use.
    //Better performance: Unlike Scrypt and Argon2, which can degrade application performance due to CPU, memory, and parallelization usage, Bcrypt provides a good balance between performance and security.

    // Detailed review of the registration process:
    // The user submits an email and password through Postman.
    // Before sending the request, a breakpoint is placed inside the encode method of the bcrypt password encoder class so that it can check the hashing process line by line.
    // Bcrypt first generates a random salt and then hashes it along with the password.
    // Bcrypt also adds a version of the algorithm (e.g. 2A) and a power level (e.g. 10) to the hash.
    // After checking this step, a new user with hashed email and password is saved in the database.
    // If two users have the same password, their hashes will be different because a new salt is generated each time.
    // User login:
    // For the login process, the user enters his email and password.
    // A breakpoint is placed inside the matches method of the bcrypt class, which is responsible for matching the entered password with the hashed password in the database.
    // This method first converts the raw password into bytes and then compares the salt and hash values.
    // Finally, if the password matches the hashed value, the user is successfully logged in.
    // Importance of using Delegating Password Encoder:
    // There is a lot of emphasis on the point that you should use delegating password encoder and never use a special encoder like bcrypt as hardware in the code.
    // The advantage of delegating password encoder is that if new hash algorithms are introduced in the next versions of Spring Security, there is no need to change the code. Spring Security itself selects the appropriate algorithm according to the hash prefix.

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * From Spring  Security 6.3 version
     */
    // CompromisedPasswordChecker: An API for checking if a password has been compromised
    // When you enter the simple password, it gives this message (The provided password is compromised, please change your password)
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
