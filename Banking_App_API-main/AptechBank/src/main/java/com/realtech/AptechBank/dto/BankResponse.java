package com.realtech.AptechBank.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankResponse {
    private String statusCode;
    private String statusMessage;
    AccountInfo accountInfo;
}