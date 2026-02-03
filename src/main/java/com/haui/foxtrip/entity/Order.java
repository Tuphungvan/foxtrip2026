package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.OrderStatus;
import com.haui.foxtrip.enums.PaymentStatus;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_number", columnList = "order_number"),
    @Index(name = "idx_order_user", columnList = "user_id, created_at"),
    @Index(name = "idx_order_status", columnList = "status, payment_status"),
    @Index(name = "idx_order_date", columnList = "created_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {
    
    @Column(name = "order_number", unique = true, nullable = false)
    String orderNumber;
    
    @Column(name = "user_id", nullable = false)
    UUID userId; // Keycloak user ID
    
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    BigDecimal totalAmount;
    
    @Builder.Default
    @Column(name = "discount_amount", precision = 15, scale = 2)
    BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "final_amount", nullable = false, precision = 15, scale = 2)
    BigDecimal finalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    PaymentStatus paymentStatus;
    
    @Type(JsonBinaryType.class)
    @Column(name = "customer_info", columnDefinition = "jsonb", nullable = false)
    Map<String, Object> customerInfo;
    
    @Column(columnDefinition = "TEXT")
    String notes;
    
    @Column(name = "confirmed_at")
    LocalDateTime confirmedAt;
    
    @Column(name = "completed_at")
    LocalDateTime completedAt;
}
