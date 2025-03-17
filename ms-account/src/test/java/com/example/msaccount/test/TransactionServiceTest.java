package com.example.msaccount.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.msaccount.dto.TransactionDto;
import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.Transaction;
import com.example.msaccount.entity.TransactionStatus;
import com.example.msaccount.entity.TransactionType;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.repository.TransactionRepository;
import com.example.msaccount.service.AccountService;
import com.example.msaccount.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account account;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setAccountId(1L);
        account.setUserId(1L);
        account.setBalance(BigDecimal.valueOf(100));

        transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.valueOf(50));
        transaction.setTransactionType(TransactionType.PURCHASE);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
    }

    @Test
    void testPurchase_Success() {
        when(accountService.findByAccountId(1L, 1L)).thenReturn(account);

        transactionService.purchase(1L, BigDecimal.valueOf(50), 1L);

        assertEquals(BigDecimal.valueOf(50), account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testPurchase_InsufficientBalance() {
        when(accountService.findByAccountId(1L, 1L)).thenReturn(account);

        transactionService.purchase(1L, BigDecimal.valueOf(150), 1L);

        assertEquals(BigDecimal.valueOf(100), account.getBalance()); // Balance should remain the same
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testRefund_Success() {
        when(transactionRepository.findByTransactionIdAndUserIdAndAccountIdAndTransactionTypeAndTransactionStatus(
                anyLong(), anyLong(), anyLong(), any(), any()
        )).thenReturn(Optional.of(transaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.existsByParentTransactionIdAndTransactionTypeAndUserIdAndAccountId(anyLong(), any(), anyLong(), anyLong()))
                .thenReturn(false);

        transactionService.refund(1L, 1L, 1l);

        assertEquals(BigDecimal.valueOf(150), account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testRefund_TransactionNotFound() {
        when(transactionRepository.findByTransactionIdAndUserIdAndAccountIdAndTransactionTypeAndTransactionStatus(
                anyLong(), anyLong(), anyLong(), any(), any()
        )).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> transactionService.refund(1L, 1L, 1l));
    }

    @Test
    void testRefund_AlreadyRefunded() {
        when(transactionRepository.findByTransactionIdAndUserIdAndAccountIdAndTransactionTypeAndTransactionStatus(
                anyLong(), anyLong(), anyLong(), any(), any()
        )).thenReturn(Optional.of(transaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.existsByParentTransactionIdAndTransactionTypeAndUserIdAndAccountId(anyLong(), any(), anyLong(), anyLong()))
                .thenReturn(true);

        transactionService.refund(1L, 1L, 1l);

        assertEquals(BigDecimal.valueOf(100), account.getBalance()); // Balance should remain the same
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testTopUp() {
        when(accountService.findByAccountId(1L, 1L)).thenReturn(account);

        transactionService.topUp(1L, BigDecimal.valueOf(50), 1L);

        assertEquals(BigDecimal.valueOf(150), account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testFindTransactionsByAccountId() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1L);
        transaction1.setParentTransactionId(null);
        transaction1.setTransactionType(TransactionType.PURCHASE);
        transaction1.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction1.setAmount(BigDecimal.valueOf(50));
        transaction1.setDate(LocalDateTime.now());
        transaction1.setInfo("Purchase 1");

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2L);
        transaction2.setParentTransactionId(1L);
        transaction2.setTransactionType(TransactionType.REFUND);
        transaction2.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction2.setAmount(BigDecimal.valueOf(50));
        transaction2.setDate(LocalDateTime.now());
        transaction2.setInfo("Refund 1");

        when(transactionRepository.findByAccountIdAndUserIdOrderByDateDesc(1L, 1L))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        List<TransactionDto> transactionDtos = transactionService.findTransactionsByAccountId(1L, 1L);

        assertNotNull(transactionDtos);
        assertEquals(2, transactionDtos.size());

        TransactionDto dto1 = transactionDtos.get(0);
        assertEquals(1L, dto1.getTransactionId());
        assertNull(dto1.getParentTransactionId());
        assertEquals("PURCHASE", dto1.getTransactionType());
        assertEquals("SUCCESS", dto1.getTransactionStatus());
        assertEquals(BigDecimal.valueOf(50), dto1.getAmount());
        assertNotNull(dto1.getDate());
        assertEquals("Purchase 1", dto1.getInfo());

        TransactionDto dto2 = transactionDtos.get(1);
        assertEquals(2L, dto2.getTransactionId());
        assertEquals(1L, dto2.getParentTransactionId());
        assertEquals("REFUND", dto2.getTransactionType());
        assertEquals("SUCCESS", dto2.getTransactionStatus());
        assertEquals(BigDecimal.valueOf(50), dto2.getAmount());
        assertNotNull(dto2.getDate());
        assertEquals("Refund 1", dto2.getInfo());

        verify(transactionRepository, times(1))
                .findByAccountIdAndUserIdOrderByDateDesc(1L, 1L);
    }
}

