package com.example.demo.controllers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/register", produces = "text/html")
@Controller
@SpringBootApplication
@RestController

public class StaticResourceController {

    @GetMapping("/register")
    public String serveRegisterPage() {
        return "register.html";
    }
}