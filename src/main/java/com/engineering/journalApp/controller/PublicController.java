package com.engineering.journalApp.controller;

import com.engineering.journalApp.entity.User;
import com.engineering.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/create-user")
    public boolean createUser(@RequestBody User user){
        userService.saveEntry(user);
        return true;
    }
}
