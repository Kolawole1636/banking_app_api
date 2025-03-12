package com.realtech.AptechBank.service;
//
import com.realtech.AptechBank.entity.UserData;
import com.realtech.AptechBank.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {


    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public void saveUser(UserData userData) {
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));

        userDataRepository.save(userData);

    }

    public String verify(UserData userData) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userData.getName(), userData.getPassword()));

        if(authentication.isAuthenticated()){

            return jwtService.generateToken(userData.getName());
        }

        return "fail";
    }
}
