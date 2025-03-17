package com.example.msaccount.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long transactionId;
    private Long parentTransactionId;
    private String transactionType;
    private String transactionStatus;
    private BigDecimal amount;
    private LocalDateTime date;
    private String info;
}
