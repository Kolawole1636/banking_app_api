package com.realtech.AptechBank.controller;

//
import com.realtech.AptechBank.entity.UserData;
//import com.realtech.AptechBank.service.UserDataService;
import com.realtech.AptechBank.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserDataService userDataservice;

    @PostMapping("/register")
    public String saveUser(@RequestBody UserData userData){

        userDataservice.saveUser(userData);

        return "User created successfully";

    }


    @PostMapping("/login")
    public String login(@RequestBody UserData userData){

        return userDataservice.verify(userData);

        //return "User login successfully";

    }





}
