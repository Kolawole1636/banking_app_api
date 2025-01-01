package com.realtech.AptechBank.service;

import com.realtech.AptechBank.dto.TransactionDto;
import com.realtech.AptechBank.entity.Transaction;
import com.realtech.AptechBank.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    public void saveTransaction(TransactionDto transactionDto){

        Transaction transaction = Transaction.builder()
                .accountNumber(transactionDto.getAccountNumber())
                .transactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepo.save(transaction);


    }
}
