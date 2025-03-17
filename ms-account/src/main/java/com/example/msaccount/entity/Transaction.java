package com.example.msaccount.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long parentTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Account account;

    @Column(name = "account_id")
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String info;

    private BigDecimal amount;

    private LocalDateTime date;

    private Long userId;
}
