package com.example.mspayment.repository;

import com.example.mspayment.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, Long> {
    boolean existsByIdempotencyKey(String idempotencyKey);

}
