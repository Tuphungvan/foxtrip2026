package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.PaymentMethod;
import com.haui.foxtrip.enums.PaymentStatus;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_order", columnList = "order_id"),
    @Index(name = "idx_payment_transaction", columnList = "transaction_id"),
    @Index(name = "idx_payment_status", columnList = "status, created_at"),
    @Index(name = "idx_payment_expires", columnList = "expires_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;
    
    @Column(name = "payment_provider")
    String paymentProvider = "sepay";
    
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal amount;
    
    @Column(name = "transaction_id", unique = true)
    String transactionId;
    
    @Column(name = "sepay_order_id")
    String sepayOrderId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PaymentStatus status;
    
    @Type(JsonBinaryType.class)
    @Column(name = "gateway_response", columnDefinition = "jsonb")
    Map<String, Object> gatewayResponse;
    
    @Column(name = "paid_at")
    LocalDateTime paidAt;
    
    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;
}
