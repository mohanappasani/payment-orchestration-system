package com.paymentOrchestration.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.paymentOrchestration.enums.PaymentStatus;
import com.paymentOrchestration.enums.PaymentType;
import com.paymentOrchestration.enums.ProviderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String merchantId;

    private BigDecimal amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(unique = true)
    private String idempotencyKey;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}