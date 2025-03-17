package com.example.msaccount.repository;

import com.example.msaccount.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountIdAndUserId(Long accountId, Long userId);
}