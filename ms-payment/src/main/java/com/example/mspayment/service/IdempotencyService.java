package com.example.mspayment.service;

import com.example.mspayment.entity.IdempotencyKey;
import com.example.mspayment.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public boolean isProcessed(String idempotencyKey) {
        boolean isProcessed = idempotencyKeyRepository.existsByIdempotencyKey(idempotencyKey);
        if (!isProcessed) {
            markAsProcessed(idempotencyKey);
        }
        return isProcessed;
    }

    private void markAsProcessed(String idempotencyKey) {
        IdempotencyKey key = new IdempotencyKey();

        key.setIdempotencyKey(idempotencyKey);
        key.setProcessedAt(LocalDateTime.now());
        idempotencyKeyRepository.save(key);
    }
}
