package com.example.SpringSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {

    @GetMapping("/myNotice")
    public String getNotices(){
        return "Here are the notices details from the DB";
    }

}
