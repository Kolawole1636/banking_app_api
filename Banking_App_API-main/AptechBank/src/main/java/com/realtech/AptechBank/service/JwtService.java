package com.realtech.AptechBank.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private String secretKey = "";

    public JwtService() throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGen.generateKey();
        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());


    }

    public void generateToken(String name) {

        Map<String, Object> claims = new HashMap<>();

        Jwts.builder()
                .claims()
                .add(claims)
                .subject(name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 +30))
                .and()
                .signWith(getKey())
                .compact();

    }

    public Key getKey(){

        byte[] keyByte = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyByte);


    }


    public String extractUserName(String token) {

        return null;

    }


    public boolean validateToke(String token, UserDetails userDetails) {

        return true;
    }





}
