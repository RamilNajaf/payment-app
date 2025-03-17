package com.example.msaccount.service;

import com.example.msaccount.dto.TransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeadLetterConsumer {
    @KafkaListener(topics = "${spring.kafka.topics.transaction-dlt}",
            groupId = "dead-letter-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeDeadLetterMessages(TransactionRequest transactionRequest) {
        log.error("Error while consuming {}", transactionRequest);
    }
}

