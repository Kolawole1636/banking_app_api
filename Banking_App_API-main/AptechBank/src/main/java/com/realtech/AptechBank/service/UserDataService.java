package com.realtech.AptechBank.service;
//
//import com.realtech.AptechBank.entity.UserData;
//import com.realtech.AptechBank.repository.UserDataRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDataService {
//
//
//    @Autowired
//    UserDataRepository userDataRepository;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    public void saveUser(UserData userData) {
//        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
//
//        userDataRepository.save(userData);
//
//    }
//}
