package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountDto;

import com.example.msaccount.dto.TransactionDto;
import com.example.msaccount.service.AccountService;
import com.example.msaccount.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    public static final String USER_ID = "X-USER-ID";

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestHeader(USER_ID) Long userId) {
        return new ResponseEntity<>(new AccountDto(accountService.createAccount(userId)), HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> findByAccountId(@RequestHeader(USER_ID) Long userId,
                                                      @PathVariable Long accountId) {
        return new ResponseEntity<>(new AccountDto(accountService.findByAccountId(accountId, userId)), HttpStatus.OK);
    }


    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDto>> findTransactionsByAccountId(@RequestHeader(USER_ID) Long userId,
                                                                            @PathVariable Long accountId) {
        return new ResponseEntity<>(transactionService.findTransactionsByAccountId(accountId, userId), HttpStatus.OK);
    }

}
