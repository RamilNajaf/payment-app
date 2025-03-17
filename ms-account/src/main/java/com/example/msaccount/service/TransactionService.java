package com.example.msaccount.service;


import com.example.msaccount.dto.TransactionDto;
import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.Transaction;
import com.example.msaccount.entity.TransactionStatus;
import com.example.msaccount.entity.TransactionType;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {


    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance for the purchase with accountId: ";
    private static final String TRANSACTION_NOT_FOUND_MESSAGE = "Transaction not found with transactionId: ";
    private static final String TRANSACTION_NOT_FOUND_MESSAGE_2 = "Transaction not found with transactionAccountId: ";
    private static final String TRANSACTION_ALREADY_REFUNDED_MESSAGE = "Transaction already refunded for transactionId: ";


    @Transactional
    public void purchase(Long accountId, BigDecimal amount, Long userId) {
        Account account = accountService.findByAccountId(accountId, userId);

        if (account.getBalance().compareTo(amount) < 0) {
            logAndSaveTransactionFailure(accountId, amount, INSUFFICIENT_BALANCE_MESSAGE + accountId, TransactionType.PURCHASE, userId);
            return;
        }

        account.setBalance(account.getBalance().subtract(amount));

        saveSuccessTransaction(accountId, amount, TransactionType.PURCHASE, null, userId);
    }


    @Transactional
    public void refund(Long accountId, Long transactionId, Long userId) {

        Transaction originalTransaction = transactionRepository
                .findByTransactionIdAndUserIdAndAccountIdAndTransactionTypeAndTransactionStatus(transactionId,
                        userId,
                        accountId,
                        TransactionType.PURCHASE,
                        TransactionStatus.SUCCESS)
                .orElseThrow(() -> new NoSuchElementException(TRANSACTION_NOT_FOUND_MESSAGE + transactionId));


        Account account = accountRepository.findById(originalTransaction.getAccount().getAccountId())
                .orElseThrow(() -> new NoSuchElementException(TRANSACTION_NOT_FOUND_MESSAGE_2 +
                        originalTransaction.getAccount().getAccountId()));

        BigDecimal refundAmount = originalTransaction.getAmount();


        boolean isAlreadyRefunded = transactionRepository.existsByParentTransactionIdAndTransactionTypeAndUserIdAndAccountId(transactionId,
                TransactionType.REFUND,
                userId, accountId);

        if (isAlreadyRefunded) {
            logAndSaveTransactionFailure(account.getAccountId(), refundAmount,
                    TRANSACTION_ALREADY_REFUNDED_MESSAGE + transactionId, TransactionType.REFUND, userId);
            return;
        }


        account.setBalance(account.getBalance().add(refundAmount));

        saveSuccessTransaction(account.getAccountId(), refundAmount, TransactionType.REFUND, transactionId, userId);
    }


    @Transactional
    public void topUp(Long accountId, BigDecimal amount, Long userId) {
        Account account = accountService.findByAccountId(accountId, userId);

        account.setBalance(account.getBalance().add(amount));

        saveSuccessTransaction(account.getAccountId(), amount, TransactionType.TOP_UP, null, userId);
    }

    public List<TransactionDto> findTransactionsByAccountId(Long accountId, Long userId) {
        return transactionRepository.findByAccountIdAndUserIdOrderByDateDesc(accountId, userId)
                .stream()
                .map(transaction -> {
                    TransactionDto dto = new TransactionDto();
                    dto.setTransactionId(transaction.getTransactionId());
                    dto.setParentTransactionId(transaction.getParentTransactionId());
                    dto.setTransactionType(transaction.getTransactionType().name());
                    dto.setTransactionStatus(transaction.getTransactionStatus().name());
                    dto.setAmount(transaction.getAmount());
                    dto.setDate(transaction.getDate());
                    dto.setInfo(transaction.getInfo());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    private void logAndSaveTransactionFailure(Long accountId, BigDecimal amount, String message, TransactionType type, Long userId) {
        Transaction failedTransaction = new Transaction();
        failedTransaction.setAccountId(accountId);
        failedTransaction.setTransactionType(type);
        failedTransaction.setTransactionStatus(TransactionStatus.FAIL);
        failedTransaction.setAmount(amount);
        failedTransaction.setDate(LocalDateTime.now());
        failedTransaction.setInfo(message);
        failedTransaction.setUserId(userId);
        transactionRepository.save(failedTransaction);
        log.warn(message);
    }

    private void saveSuccessTransaction(Long accountId, BigDecimal amount, TransactionType type, Long parentTransactionId, Long userId) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setTransactionType(type);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setParentTransactionId(parentTransactionId);
        transaction.setInfo("Success " + type);
        transaction.setUserId(userId);
        transactionRepository.save(transaction);
        log.info("Transaction of type {} for accountId {} processed successfully.", type, accountId);
    }
}
