package com.zemoso.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied(){
        return "access-denied";
    }

}
