package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.DiscountType;
import com.haui.foxtrip.enums.VoucherStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vouchers", indexes = {
    @Index(name = "idx_voucher_code", columnList = "code"),
    @Index(name = "idx_voucher_status", columnList = "status, valid_from, valid_to"),
    @Index(name = "idx_voucher_dates", columnList = "valid_from, valid_to")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    String code;
    
    @Column(nullable = false)
    String name;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    DiscountType discountType;
    
    @Column(name = "discount_value", nullable = false, precision = 15, scale = 2)
    BigDecimal discountValue;
    
    @Column(name = "min_order_amount", precision = 15, scale = 2)
    BigDecimal minOrderAmount = BigDecimal.ZERO;
    
    @Column(name = "max_discount_amount", precision = 15, scale = 2)
    BigDecimal maxDiscountAmount;
    
    @Column(name = "usage_limit")
    Integer usageLimit;
    
    @Column(name = "usage_per_user")
    Integer usagePerUser = 1;
    
    @Column(name = "valid_from", nullable = false)
    LocalDateTime validFrom;
    
    @Column(name = "valid_to", nullable = false)
    LocalDateTime validTo;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "applicable_item_types", columnDefinition = "varchar[]")
    String[] applicableItemTypes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    VoucherStatus status;
    
    @Column(name = "created_by")
    UUID createdBy;
}
