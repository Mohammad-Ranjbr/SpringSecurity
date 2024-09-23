package com.eazybytes.eazyschool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public String displayLoginPage(@RequestParam(value = "error", required = false) String error
            , Model model, @RequestParam(value = "logout", required = false) String logout) {
        String errorMessge = null;
        if(null != error){
            errorMessge = "Username or Password is incorrect !!";
        }
        if(null != logout){
            errorMessge = "You have been successfully logged out!!";
        }
        model.addAttribute("errorMessage",errorMessge);
        return "login.html";
    }

}