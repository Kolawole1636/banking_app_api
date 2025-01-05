package com.realtech.AptechBank.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.realtech.AptechBank.dto.EmailDetails;
import com.realtech.AptechBank.entity.Transaction;
import com.realtech.AptechBank.entity.UserInfo;
import com.realtech.AptechBank.repository.TransactionRepo;
import com.realtech.AptechBank.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.css.Rect;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BankStatement {

/*
1. retrieve list of transactions within a date range
2. generate the pdf for those transactions
3. send it to the user via email.

 */
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    private static final String FILE = "C:\\Users\\HP\\Desktop\\bankingappspringboot\\bank_statement.pdf";


    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws Exception {

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactions = transactionRepo.findAll().stream()
                .filter(transaction->transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction->transaction.getDateCreated().isEqual(start))
                .filter(transaction->transaction.getDateCreated().isEqual(end))
                .toList();

        UserInfo user = userInfoRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName()+" "+ user.getLastName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("SMA Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.CYAN);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("SMA Bank"));
        bankAddress .setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date"+ startDate));
        customerInfo.setBorder(0);

        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);

        PdfPCell stopDate = new PdfPCell(new Phrase("End Date"+ endDate));
        stopDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name"+ customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();

        PdfPCell address = new PdfPCell(new Phrase("Customer Address"+ user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);

        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.BLUE);

        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBorder(0);
        transactionType.setBackgroundColor(BaseColor.BLUE);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBorder(0);
        transactionAmount.setBackgroundColor(BaseColor.BLUE);

        PdfPCell transactionStatus = new PdfPCell(new Phrase("TRANSACTION STATUS"));
        transactionStatus.setBorder(0);
        transactionStatus.setBackgroundColor(BaseColor.BLUE);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(transactionStatus);

        transactions.forEach(transaction->{

            transactionTable.addCell(new Phrase(transaction.getDateCreated().toString()));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionTable.addCell(new Phrase(transaction.getStatus()));


        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);


        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested statement of account attached!")
                .attachment(FILE)

                .build();
        emailService.sendMailWithAttachment(emailDetails);

        return transactions;


    }

}
