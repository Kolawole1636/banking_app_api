package com.realtech.AptechBank.controller;


import com.realtech.AptechBank.entity.Transaction;
import com.realtech.AptechBank.service.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankStatementController {

    @Autowired
    private BankStatement bankStatement;

    @GetMapping("bankstatement")

    public List<Transaction> generateStatement(@RequestParam String accountNumber,
                                               @RequestParam String startDate,
                                               @RequestParam String endDate
                                               ) throws Exception {

        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }


}
