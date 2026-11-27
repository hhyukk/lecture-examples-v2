package com.growmighty.lectures.firstday.tangledmonolith.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment pay(BigDecimal totalAmount) {
        Payment payment = Payment.ready(totalAmount);
        // 새 메서드로 수정
        payment.approve();
        paymentRepository.save(payment);
        return payment;
    }
}
