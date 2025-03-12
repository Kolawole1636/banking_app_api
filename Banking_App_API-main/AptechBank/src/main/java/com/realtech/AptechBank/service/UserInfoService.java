package com.realtech.AptechBank.service;
import com.realtech.AptechBank.dto.*;
import com.realtech.AptechBank.entity.UserInfo;
import com.realtech.AptechBank.repository.UserInfoRepository;
import com.realtech.AptechBank.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private EmailService emailService;

    public BankResponse createAccount(UserRequest userRequest) {
        BankResponse bankResponse = new BankResponse();
        if (userInfoRepository.existsByEmail(userRequest.getEmail())) {
            bankResponse.setStatusCode(AccountUtil.EMAIL_EXIST_CODE);
            bankResponse.setStatusMessage(AccountUtil.EMAIL_EXIST_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }


        UserInfo userInfo = UserInfo.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .build();

        UserInfo saveUser = userInfoRepository.save(userInfo);

        EmailDetails details = new EmailDetails();
        details.setRecipient(saveUser.getEmail());
        details.setSubject("ACCOUNT CREATION");
        details.setMessageBody("Welcome To SMA Bank our new customer, your account has been successfully created"+"\n"
                + "Account Number:" + saveUser.getAccountNumber() + "\n"
                + "Account Balance:" + saveUser.getAccountBalance() + "\n"
                + "Account Name:" + saveUser.getFirstName() + " " + saveUser.getLastName());
        emailService.sendMail(details);

        bankResponse.setStatusCode(AccountUtil.ACCOUNT_CREATED_CODE);
        bankResponse.setStatusMessage(AccountUtil.ACCOUNT_CREATED_MESSAGE);
        bankResponse.setAccountInfo(AccountInfo.builder()
                .accountNumber(saveUser.getAccountNumber())
                .accountBalance(saveUser.getAccountBalance())
                .accountName(saveUser.getFirstName() + " " + saveUser.getLastName())
                .build());
        return bankResponse;
    }

    public List<AccountInfo> allUsers(){
        List<UserInfo> users = userInfoRepository.findAll();
        List<AccountInfo> allUsers = new ArrayList<>();
        for(UserInfo user:users){
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountNumber(user.getAccountNumber());
            accountInfo.setAccountBalance(user.getAccountBalance());
            accountInfo.setAccountName(user.getFirstName()+" "+user.getLastName());
            allUsers.add(accountInfo);
        }
        return allUsers;
    }


    public String getAccountName(EnquiryRequest request){
        boolean account_exist = userInfoRepository.existsByAccountNumber(request.getAccountNumber());
        if(!account_exist){
            return AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        UserInfo user = userInfoRepository.findByAccountNumber(request.getAccountNumber());
        return user.getFirstName() +" "+ user.getFirstName();
    }

    public BankResponse getBalance(EnquiryRequest request) {
        BankResponse bankResponse = new BankResponse();
        AccountInfo accountInfo = new AccountInfo();
        boolean account_exist = userInfoRepository.existsByAccountNumber(request.getAccountNumber());
        if (!account_exist) {
            bankResponse.setStatusCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        UserInfo user = userInfoRepository.findByAccountNumber(request.getAccountNumber());
        bankResponse.setStatusCode(AccountUtil.ACCOUNT_FOUND_CODE);
        bankResponse.setStatusMessage(AccountUtil.ACCOUNT_FOUND_MESSAGE);
        accountInfo.setAccountNumber(request.getAccountNumber());
        accountInfo.setAccountBalance(user.getAccountBalance());
        accountInfo.setAccountName(user.getFirstName() + " " + user.getLastName());
        bankResponse.setAccountInfo(accountInfo);
        return bankResponse;
    }

    public BankResponse creditAccount(CreditDebitRequest request){
        BankResponse bankResponse = new BankResponse();
        AccountInfo accountInfo = new AccountInfo();
        boolean account_exist = userInfoRepository.existsByAccountNumber(request.getAccountNumber());
        if (!account_exist) {
            bankResponse.setStatusCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        UserInfo userToCredit = userInfoRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userInfoRepository.save(userToCredit);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .transactionType("CREDIT")
                .build();

        transactionService.saveTransaction(transactionDto);

        EmailDetails details = new EmailDetails();
        details.setRecipient(userToCredit.getEmail());
        details.setSubject("ACCOUNT CREDITED");
        details.setMessageBody("your account has been credited"+"\n"
                + "Amount:" + "NGN"+ request.getAmount() +"\n"
                + "Account Balance:" + userToCredit.getAccountBalance() + "\n"
                + "Account Name:" + userToCredit.getFirstName() + " " + userToCredit.getLastName());
        emailService.sendMail(details);

        bankResponse.setStatusCode(AccountUtil.ACCOUNT_CREDIT_CODE);
        bankResponse.setStatusMessage(AccountUtil.ACCOUNT_CREDIT_MESSAGE);
        accountInfo.setAccountNumber(request.getAccountNumber());
        accountInfo.setAccountBalance(userToCredit.getAccountBalance());
        accountInfo.setAccountName(userToCredit.getFirstName() + " " + userToCredit.getLastName());
        bankResponse.setAccountInfo(accountInfo);
        return bankResponse;
    }

    public BankResponse debitAccount(CreditDebitRequest request){
        BankResponse bankResponse = new BankResponse();
        AccountInfo accountInfo = new AccountInfo();
        boolean account_exist = userInfoRepository.existsByAccountNumber(request.getAccountNumber());
        if (!account_exist) {
            bankResponse.setStatusCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        UserInfo userToDebit = userInfoRepository.findByAccountNumber(request.getAccountNumber());
        if(userToDebit.getAccountBalance().compareTo(request.getAmount())<0){
            bankResponse.setStatusCode(AccountUtil.INSUFFICIENT_BALANCE_CODE);
            bankResponse.setStatusMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userInfoRepository.save(userToDebit);

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .amount(request.getAmount())
                    .transactionType("DEBIT")
                    .build();

            transactionService.saveTransaction(transactionDto);

            EmailDetails details = new EmailDetails();
            details.setRecipient(userToDebit.getEmail());
            details.setSubject("ACCOUNT DEBITED");
            details.setMessageBody("your account has been debited"+"\n"
                    + "Amount:" + "NGN"+ request.getAmount() +"\n"
                    + "Your current balance:" + userToDebit.getAccountBalance() + "\n"
                    + "Account Name:" + userToDebit.getFirstName() + " " + userToDebit.getLastName());
            emailService.sendMail(details);
            bankResponse.setStatusCode(AccountUtil.ACCOUNT_DEBIT_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_DEBIT_MESSAGE);
            accountInfo.setAccountNumber(request.getAccountNumber());
            accountInfo.setAccountBalance(userToDebit.getAccountBalance());
            accountInfo.setAccountName(userToDebit.getFirstName() + " " + userToDebit.getLastName());
            bankResponse.setAccountInfo(accountInfo);
            return bankResponse;
        }
    }


    public BankResponse transfer(TransferRequest request){
        BankResponse bankResponse = new BankResponse();
        AccountInfo accountInfo = new AccountInfo();
        boolean recipient_account_exist = userInfoRepository.existsByAccountNumber(request.getRecipientAccountNumber());
        boolean sender_account_exist = userInfoRepository.existsByAccountNumber(request.getSenderAccountNumber());
        if (!recipient_account_exist || !sender_account_exist) {
            bankResponse.setStatusCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        UserInfo userToDebit = userInfoRepository.findByAccountNumber(request.getSenderAccountNumber());
        UserInfo userToCredit = userInfoRepository.findByAccountNumber(request.getRecipientAccountNumber());
        if(userToDebit.getAccountBalance().compareTo(request.getAmountToSend())<0){
            bankResponse.setStatusCode(AccountUtil.INSUFFICIENT_BALANCE_CODE);
            bankResponse.setStatusMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmountToSend()));
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmountToSend()));
            userInfoRepository.save(userToDebit);
            userInfoRepository.save(userToCredit);

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .amount(request.getAmountToSend())
                    .transactionType("TRANSFER_DEB")
                    .build();


            TransactionDto transactionDto1 = TransactionDto.builder()
                    .accountNumber(userToCredit.getAccountNumber())
                    .amount(request.getAmountToSend())
                    .transactionType("TRANSFER_CRED")
                    .build();

            transactionService.saveTransaction(transactionDto);
            transactionService.saveTransaction(transactionDto1);

            EmailDetails details = new EmailDetails();
            details.setRecipient(userToDebit.getEmail());
            details.setSubject("ACCOUNT DEBITED");
            details.setMessageBody("your account has been debited"+"\n"
                    + "Amount Transferred:" + "NGN"+ request.getAmountToSend() +"\n"
                    + "Your current balance:" + userToDebit.getAccountBalance() + "\n"
                    + "Account Name:" + userToDebit.getFirstName() + " " + userToDebit.getLastName()+"\n"
                    + "Recipient Name:" + userToCredit.getFirstName() + " " + userToCredit.getLastName());

            emailService.sendMail(details);

            EmailDetails creditDetails = new EmailDetails();
            creditDetails.setRecipient(userToCredit.getEmail());
            creditDetails.setSubject("ACCOUNT CREDITED");
            creditDetails.setMessageBody("your account has been credited"+"\n"
                    + "Amount Transferred to you:" + "NGN"+ request.getAmountToSend() +"\n"
                    + "Your current balance:" + userToCredit.getAccountBalance() + "\n"
                    + "Account Name:" + userToCredit.getFirstName() + " " + userToCredit.getLastName()+"\n"
                    + "Sender Name:" + userToDebit.getFirstName() + " " + userToDebit.getLastName());

            emailService.sendMail(creditDetails);

            bankResponse.setStatusCode(AccountUtil.ACCOUNT_DEBIT_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_DEBIT_MESSAGE);
            accountInfo.setAccountNumber(request.getSenderAccountNumber());
            accountInfo.setAccountBalance(userToDebit.getAccountBalance());
            accountInfo.setAccountName(userToDebit.getFirstName() + " " + userToDebit.getLastName());
            bankResponse.setAccountInfo(accountInfo);
            return bankResponse;
        }
    }


    public BankResponse multipleTransfer(MultipleTransferRequest request){
        BankResponse bankResponse = new BankResponse();
        AccountInfo accountInfo = new AccountInfo();
        boolean first_recipient_account_exist = userInfoRepository.existsByAccountNumber(request.getFirstRecipientAccountNumber());
        boolean second_recipient_account_exist = userInfoRepository.existsByAccountNumber(request.getSecondRecipientAccountNumber());
        boolean sender_account_exist = userInfoRepository.existsByAccountNumber(request.getSenderAccountNumber());
        if (!first_recipient_account_exist || !second_recipient_account_exist || !sender_account_exist) {
            bankResponse.setStatusCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        UserInfo userToDebit = userInfoRepository.findByAccountNumber(request.getSenderAccountNumber());
        UserInfo firstUserToCredit = userInfoRepository.findByAccountNumber(request.getFirstRecipientAccountNumber());
        UserInfo secondUserToCredit = userInfoRepository.findByAccountNumber(request.getSecondRecipientAccountNumber());
        BigDecimal totalAmountToSend = request.getFirstRecipientAmountToSend().add(request.getSecondRecipientAmountToSend());

        if(userToDebit.getAccountBalance().compareTo(totalAmountToSend)<0){
            bankResponse.setStatusCode(AccountUtil.INSUFFICIENT_BALANCE_CODE);
            bankResponse.setStatusMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE);
            bankResponse.setAccountInfo(null);
            return bankResponse;
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(totalAmountToSend));
            firstUserToCredit.setAccountBalance(userToDebit.getAccountBalance().add(request.getFirstRecipientAmountToSend()));
            secondUserToCredit.setAccountBalance(userToDebit.getAccountBalance().add(request.getSecondRecipientAmountToSend()));
            userInfoRepository.save(userToDebit);
            userInfoRepository.save(firstUserToCredit);
            userInfoRepository.save(secondUserToCredit);

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .amount(totalAmountToSend)
                    .transactionType("DOUBLE TRANSFER")
                    .build();

            EmailDetails details = new EmailDetails();
            details.setRecipient(userToDebit.getEmail());
            details.setSubject("ACCOUNT DEBITED");
            details.setMessageBody("your account has been debited"+"\n"
                    + "Your current balance:" + userToDebit.getAccountBalance() + "\n"
                    + "Account Name:" + userToDebit.getFirstName() + " " + userToDebit.getLastName());
            emailService.sendMail(details);

            bankResponse.setStatusCode(AccountUtil.ACCOUNT_DEBIT_CODE);
            bankResponse.setStatusMessage(AccountUtil.ACCOUNT_DEBIT_MESSAGE);
            accountInfo.setAccountNumber(request.getSenderAccountNumber());
            accountInfo.setAccountBalance(userToDebit.getAccountBalance());
            accountInfo.setAccountName(userToDebit.getFirstName() + " " + userToDebit.getLastName());
            bankResponse.setAccountInfo(accountInfo);
            return bankResponse;
        }
    }

    public String removeUser(int id) {

        userInfoRepository.deleteById(id);

        return "User has been removed successfully";
    }
}