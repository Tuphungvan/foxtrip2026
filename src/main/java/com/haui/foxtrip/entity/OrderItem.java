package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.ItemType;
import com.haui.foxtrip.enums.OrderStatus;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_item_order", columnList = "order_id"),
    @Index(name = "idx_order_item_type", columnList = "item_type, item_id"),
    @Index(name = "idx_order_item_status", columnList = "status")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    ItemType itemType;
    
    @Column(name = "item_id", nullable = false)
    UUID itemId;
    
    @Type(JsonBinaryType.class)
    @Column(name = "saved_info", columnDefinition = "jsonb", nullable = false)
    Map<String, Object> savedInfo;
    
    @Builder.Default
    @Column(nullable = false)
    Integer quantity = 1;
    
    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    BigDecimal unitPrice;
    
    @Builder.Default
    @Column(precision = 15, scale = 2)
    BigDecimal discount = BigDecimal.ZERO;
    
    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    BigDecimal totalPrice;
    
    @Type(JsonBinaryType.class)
    @Column(name = "booking_details", columnDefinition = "jsonb", nullable = false)
    Map<String, Object> bookingDetails;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;
}
