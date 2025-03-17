package com.example.mspayment.contoller;

import com.example.mspayment.dto.TransactionRequest;
import com.example.mspayment.service.PaymentService;
import com.example.mspayment.validation.DefaultValidationGroup;
import com.example.mspayment.validation.RefundValidationGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    public static final String USER_ID = "X-USER-ID";
    public static final String IDEMPOTENCY_KEY = "IDEMPOTENCY-KEY";

    @PostMapping("/top_up")
    public ResponseEntity<?> topUp(
            @RequestHeader(USER_ID) Long userId,
            @RequestHeader(IDEMPOTENCY_KEY) String idempotencyKey,
            @Validated({DefaultValidationGroup.class}) @RequestBody TransactionRequest request) {
        paymentService.topUp(request, userId, idempotencyKey);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(
            @RequestHeader(USER_ID) Long userId,
            @RequestHeader(IDEMPOTENCY_KEY) String idempotencyKey,
            @Validated({DefaultValidationGroup.class}) @RequestBody TransactionRequest request) {
        paymentService.purchase(request, userId, idempotencyKey);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestHeader(USER_ID) Long userId,
                                    @RequestHeader(IDEMPOTENCY_KEY) String idempotencyKey,
                                    @Validated(RefundValidationGroup.class)
                                    @RequestBody TransactionRequest request) {
        paymentService.refund(request, userId, idempotencyKey);
        return new ResponseEntity(HttpStatus.OK);
    }
}
