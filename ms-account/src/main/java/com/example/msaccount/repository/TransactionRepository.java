package com.example.msaccount.repository;

import com.example.msaccount.entity.Transaction;
import com.example.msaccount.entity.TransactionStatus;
import com.example.msaccount.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionIdAndUserIdAndAccountIdAndTransactionTypeAndTransactionStatus(Long transactionId,
                                                                                                         Long userId,
                                                                                                         Long accountId,
                                                                                                         TransactionType type,
                                                                                                         TransactionStatus status);

    Boolean existsByParentTransactionIdAndTransactionTypeAndUserIdAndAccountId(Long transactionId,
                                                                               TransactionType type,
                                                                               Long userId,
                                                                               Long accountId);

    List<Transaction> findByAccountIdAndUserIdOrderByDateDesc(Long accountId, Long userId);
}
