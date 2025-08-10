package com.engineering.journalApp.controller;

import com.engineering.journalApp.entity.JournalEntry;
import com.engineering.journalApp.entity.User;
import com.engineering.journalApp.service.JournalEntryService;
import com.engineering.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }

    @PostMapping
    public boolean createUser(@RequestBody User user){
        userService.saveEntry(user);
        return true;
    }

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String userName){
        User userInDb = userService.findByUserName(userName);
        if (userInDb != null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            User updated = userService.saveEntry(userInDb);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
