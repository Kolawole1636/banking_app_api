package com.realtech.AptechBank.controller;

import com.realtech.AptechBank.dto.*;
import com.realtech.AptechBank.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("bankingapp/")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/createaccount")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userInfoService.createAccount(userRequest);
    }

    @GetMapping("admin/allUsers")
    public List<AccountInfo> allUsers(){
        return userInfoService.allUsers();
    }

    @GetMapping("admin/getAccountName")
    public String getAccountName(@RequestBody EnquiryRequest request){
        return userInfoService.getAccountName(request);
    }

    @DeleteMapping("admin/removeuser/{id}")
    public String  removeUser(@PathVariable int id){
        return userInfoService.removeUser(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("getBalance")
    public BankResponse getBalance(@RequestBody EnquiryRequest request){
        return userInfoService.getBalance(request);
    }

    @PostMapping("user/creditaccount")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userInfoService.creditAccount(request);
    }

    @PostMapping("user/debitaccount")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userInfoService.debitAccount(request);
    }

    @PostMapping("user/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userInfoService.transfer(request);
    }

    @PostMapping("user/multipleTransfer")
        public BankResponse multipleTransfer(@RequestBody MultipleTransferRequest request){
        return userInfoService.multipleTransfer(request);
    }
}