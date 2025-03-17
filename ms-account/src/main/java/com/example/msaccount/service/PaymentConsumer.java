package com.example.msaccount.service;

import com.example.msaccount.dto.TransactionRequest;
import com.example.msaccount.entity.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final TransactionService transactionService;

    @KafkaListener(id = "1",
            topics = "${spring.kafka.topics.transaction}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumePayment(TransactionRequest transactionRequest) {
        System.out.println("Received payment event: " + transactionRequest);

        if (TransactionType.REFUND == transactionRequest.getType()) {
            transactionService.refund(transactionRequest.getAccountId(), transactionRequest.getTransactionId(), transactionRequest.getUserId());
        } else if (TransactionType.PURCHASE == transactionRequest.getType()) {
            transactionService.purchase(transactionRequest.getAccountId(), transactionRequest.getAmount(), transactionRequest.getUserId());
        } else if (TransactionType.TOP_UP == transactionRequest.getType()) {
            transactionService.topUp(transactionRequest.getAccountId(), transactionRequest.getAmount(), transactionRequest.getUserId());
        }
    }
}