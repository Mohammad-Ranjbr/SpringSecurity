package com.example.SpringSecurity.controller;

import com.example.SpringSecurity.model.Notice;
import com.example.SpringSecurity.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    // If he calls from a country like India (with code 91), he will answer easily and without worry, because the call is from within that country.
    // But if he receives a call from another country (for example, with the +11 code), he will be more careful, because it is possible that the call is from an unknown person or even a scammer.
    // He goes on to say that if his brother calls from London, he probably has his brother's number saved and therefore confidently answers the call, even if he is from another country.
    // CORS is a protocol that allows browsers to block HTTP requests that come from different sources
    // (different origins) by default, unless there are specific conditions where those sources are allowed to communicate.
    // Origin consists of three parts:
    // Protocol (such as HTTP or HTTPS)
    // Domain name (like google.com)
    // Port number (such as 8080)
    // If two applications from different origins want to communicate (for example, a web application on domain1.com and an API on domain2.com),
    // the browser will by default block the connection due to the CORS security policy, because the two applications They come from different sources.

    // Several solutions have been proposed to solve this problem and allow the browser to communicate between these two sources.
    // Using the @CrossOrigin Annotation:
    // This method is used in Spring Boot applications and does not require adding the Spring Security dependency.
    // By adding this annotation at the controller class level (e.g. NoticeController), you can specify that requests
    // from a specific origin (e.g. http://localhost:4200) are allowed to communicate with your server.
    // If you want to accept requests from multiple origins, you can separate the domain names with commas. You can also allow all origins by using *.
    // The problem with this method is that if you have many controllers, you have to add this annotation separately to each of them, which may be time-consuming and complicated.
    // Using Spring Security:
    // This method is done using Spring Security and offers more flexibility.
    // Here you configure CORS settings in Spring Security with the help of a Lambda expression. These settings include the determination of allowed origins (setAllowedOrigins),
    // allowed HTTP methods (setAllowedMethods), allowed headers (setAllowedHeaders) and time to cache settings (setMaxAge).

    // Preflight
    // Definition: Preflight requests are sent by the browser to check if the server is allowed to process the request from a different origin before sending the main request.
    // Performance:
    // If you use unusual HTTP methods (such as PUT or DELETE) or custom headers, the browser sends a Preflight request (with the OPTIONS method) to the server.
    // The server should respond with the appropriate headers:
    // Access-Control-Allow-Origin: Specifies which origins are allowed to access.
    // Access-Control-Allow-Methods: Specifies the allowed methods.
    // Access-Control-Allow-Headers: Specifies the allowed headers.
    // Errors: If the server does not respond or send the appropriate headers, the browser blocks the original request and issues a CORS error.

    private final NoticeRepository noticeRepository;

    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getNotices(){
        List<Notice> notices = noticeRepository.findAllActiveNotices();
        if(notices != null){
            return ResponseEntity.ok()
                    // This section adds the Cache-Control header to the response.
                    // .cacheControl() allows you to specify how to cache responses.
                    // CacheControl.maxAge(60, TimeUnit.SECONDS) says that this
                    // response should be kept in the browser or server cache for 60 seconds.
                    // This means that after receiving this response, the browser or
                    // proxy server can save this response for 60 seconds
                    // and prevent the server from sending the request again.
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .body(notices);
        } else {
            return null;
        }
    }

}
