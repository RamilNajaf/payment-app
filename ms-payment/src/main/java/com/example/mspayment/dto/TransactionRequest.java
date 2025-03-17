package com.example.mspayment.dto;

import com.example.mspayment.validation.DefaultValidationGroup;
import com.example.mspayment.validation.RefundValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotNull(groups = {DefaultValidationGroup.class, RefundValidationGroup.class}, message = "Account ID is required")
    private Long accountId;

    @NotNull(groups = DefaultValidationGroup.class, message = "Amount is required")
    @Positive(groups = DefaultValidationGroup.class, message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(groups = RefundValidationGroup.class, message = "Transaction ID is required for refund")
    private Long transactionId;

    private TransactionType type;

    private Long userId;
}
