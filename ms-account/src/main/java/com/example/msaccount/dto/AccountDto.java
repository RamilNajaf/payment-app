package com.example.msaccount.dto;

import com.example.msaccount.entity.Account;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private Long accountId;
    private Long userId;
    private BigDecimal balance;


    public AccountDto(Account account) {
        this.accountId = account.getAccountId();
        this.userId = account.getUserId();
        this.balance = account.getBalance();
    }
}
