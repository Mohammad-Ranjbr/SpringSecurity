package com.example.SpringSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    // SecurityContext is an interface in Spring Security whose main task is to maintain user authentication information during a request. During each request,
    // when the user is successfully authenticated, an Authentication object is created and stored in the SecurityContext. That way,
    // the SecurityContext gives you access to security information whenever you want to access it during that request.
    // Main function of SecurityContext:
    // Maintaining authentication information (Authentication): This interface has two key methods:
    // getAuthentication(): which returns information about the current Authentication object.
    // setAuthentication(Authentication): To save a new Authentication object.
    // The SecurityContext serves as the main repository of authentication information, and this information is kept secure during the execution of a request.
    // At the end of the application cycle, this information can be deleted or properly managed for added security.
    // SecurityContextHolder is a utility class in Spring Security that is responsible for managing and accessing SecurityContext.
    // This class actually acts as a SecurityContext holder and provides a method by which you can access authentication information during the lifecycle of a request.
    // SecurityContextHolder function:
    // Accessing the SecurityContext: Spring Security through this class allows you to access the SecurityContext object anywhere
    // in your code by calling the SecurityContextHolder.getContext() method.
    // Setting the SecurityContext: You can also define and save a new SecurityContext using the SecurityContextHolder.setContext(SecurityContext) method.
    // Clearing the SecurityContext: With the SecurityContextHolder.clearContext() method, you can clear the SecurityContext
    // after a request has ended or in certain circumstances so that no authentication information remains stored.
    // In other words, this class allows you to access or update user authentication information whenever you need to.
    // ThreadLocal: The default storage mechanism
    // ThreadLocal is used as the default method for storing SecurityContext. Using ThreadLocal, Spring Security makes it possible
    // for each thread to have its own security information. In web-based environments, each request is processed individually
    // by a single thread. Therefore, each request has its own SecurityContext and the security of authentication information is guaranteed.
    // InheritableThreadLocal:
    // This strategy is used when you need a child thread (such as an asynchronous thread) to inherit the SecurityContext information from the parent thread.
    // Example: Suppose in a web application, the user request is authenticated well, and you run an async method to perform some long-running tasks.
    // If the default ThreadLocal strategy is used, the async thread will not have the security information, but by using InheritableThreadLocal,
    // this information will be passed to the async thread and it will be able to access the same SecurityContext.
    //Global Mode:
    // In this case, all application threads have access to a common SecurityContext. This strategy is useful for single-user
    // desktop applications where all requests are made by a single user. But in web applications where multiple users access the system,
    // this strategy is risky and can lead to security mistakes.

    @GetMapping("/myAccount")
    public String getAccount(){
        return "Here are the account details from the DB";
    }

}
