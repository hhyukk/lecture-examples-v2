package com.growmighty.lectures.firstday.tangledmonolith.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    public static Payment ready(BigDecimal amount) {
        Payment payment = new Payment();
        payment.amount = amount;
        payment.status = PaymentStatus.READY;
        return payment;
    }

    //changeStatus 를 없애고 approve() / fail() 로 변경
    public void approve() {
        if (this.status!=PaymentStatus.READY) {
            throw new IllegalStateException("상태가 READY가 아닙니다.");
        }
        this.status = PaymentStatus.PAID;
    }
    public void fail() {
        if (this.status!=PaymentStatus.READY) {
            throw new IllegalStateException("상태가 READY가 아닙니다.");
        }
        this.status = PaymentStatus.FAILED;
    }
}
