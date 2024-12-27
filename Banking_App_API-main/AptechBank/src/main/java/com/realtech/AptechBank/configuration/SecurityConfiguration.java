package com.realtech.AptechBank.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfiguration {
//
//
//    @Bean
//
//    public PasswordEncoder passwordEncoder() {
//
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//
//    public UserDetailsService userDetailsService() {
//
//        return new UserDataDetailService();
//
//    }
//
//
//    @Bean
//
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//
//        return http .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(registry->{
//                    registry.requestMatchers("/createaccount","/createuser","/user").permitAll();
//                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
//                    registry.requestMatchers("/user/**").hasRole("USER");
//                    registry.anyRequest().authenticated();
//
//                }) .formLogin(AbstractAuthenticationFilterConfigurer::permitAll) .build();
//
//
//    }
//
//
//
//
//    @Bean
//
//    public AuthenticationProvider authenticationProvider() {
//
//        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
//
//        dao.setUserDetailsService(userDetailsService());
//
//        dao.setPasswordEncoder(passwordEncoder());
//
//
//        return dao;
//
//    }
//
//
//
//}
