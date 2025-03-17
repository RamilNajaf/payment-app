package com.example.msaccount.service;


import com.example.msaccount.entity.Account;
import com.example.msaccount.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountService {


    private final AccountRepository accountRepository;

    public Account createAccount(Long userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    public Account findByAccountId(Long accountId, Long userId) {
        return accountRepository.findByAccountIdAndUserId(accountId, userId)
                .orElseThrow(() -> new NoSuchElementException("Account not found"));
    }
}
