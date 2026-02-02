package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voucher_usage", indexes = {
    @Index(name = "idx_usage_voucher", columnList = "voucher_id, used_at"),
    @Index(name = "idx_usage_user", columnList = "user_id, used_at"),
    @Index(name = "idx_usage_order", columnList = "order_id")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherUsage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", nullable = false)
    Voucher voucher;
    
    @Column(name = "user_id", nullable = false)
    UUID userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    
    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    BigDecimal discountAmount;
    
    @Column(name = "used_at", nullable = false)
    LocalDateTime usedAt;
}
