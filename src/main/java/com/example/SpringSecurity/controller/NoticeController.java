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
