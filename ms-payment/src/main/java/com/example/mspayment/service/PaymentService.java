package com.example.mspayment.service;

import com.example.mspayment.dto.TransactionRequest;
import com.example.mspayment.dto.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, TransactionRequest> kafkaTemplate;
    private final IdempotencyService idempotencyService;
    private final String TOPIC = "transaction-topic";

    public void topUp(TransactionRequest transactionRequest, Long userId, String idempotencyKey) {
        if (idempotencyService.isProcessed(idempotencyKey)) {
            return;
        }
        transactionRequest.setType(TransactionType.TOP_UP);
        transactionRequest.setUserId(userId);
        kafkaTemplate.send(TOPIC, transactionRequest);
    }

    public void purchase(TransactionRequest transactionRequest, Long userId, String idempotencyKey) {
        if (idempotencyService.isProcessed(idempotencyKey)) {
            return;
        }
        transactionRequest.setType(TransactionType.PURCHASE);
        transactionRequest.setUserId(userId);
        kafkaTemplate.send(TOPIC, transactionRequest);
    }

    public void refund(TransactionRequest transactionRequest, Long userId, String idempotencyKey) {
        if (idempotencyService.isProcessed(idempotencyKey)) {
            return;
        }
        transactionRequest.setType(TransactionType.REFUND);
        transactionRequest.setUserId(userId);
        kafkaTemplate.send(TOPIC, transactionRequest);
    }
}
