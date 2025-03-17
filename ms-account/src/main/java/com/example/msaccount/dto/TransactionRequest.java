package com.example.msaccount.dto;

import com.example.msaccount.entity.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    private Long accountId;
    private BigDecimal amount;
    private Long transactionId;
    private TransactionType type;
    private Long userId;

}
