package com.vinsguru.aggregator.controller;

import com.vinsguru.aggregator.service.UserService;
import com.vinsguru.user.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserInformation getUserInformation(@PathVariable Integer userId){
        return this.userService.getUserInformation(userId);
    }

}
